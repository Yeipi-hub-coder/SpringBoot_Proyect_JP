// Utilidades de interfaz: toasts, modal genérico, confirmación, formatos.

const toastStack = document.getElementById('toast-stack');

export function toast(message, type = 'success') {
  const el = document.createElement('div');
  el.className = `toast toast-${type}`;
  el.textContent = message;
  toastStack.appendChild(el);
  setTimeout(() => el.remove(), 4200);
}

const backdrop = document.getElementById('modal-backdrop');
const modalTitle = document.getElementById('modal-title');
const modalBody = document.getElementById('modal-body');
const modalClose = document.getElementById('modal-close');

let closeCallback = null;

export function openModal(title, bodyEl, { onClose } = {}) {
  modalTitle.textContent = title;
  modalBody.innerHTML = '';
  modalBody.appendChild(bodyEl);
  backdrop.hidden = false;
  closeCallback = onClose || null;
  document.body.style.overflow = 'hidden';
}

export function closeModal() {
  backdrop.hidden = true;
  modalBody.innerHTML = '';
  document.body.style.overflow = '';
  if (closeCallback) {
    const cb = closeCallback;
    closeCallback = null;
    cb();
  }
}

modalClose.addEventListener('click', closeModal);
backdrop.addEventListener('click', (e) => {
  if (e.target === backdrop) closeModal();
});
document.addEventListener('keydown', (e) => {
  if (e.key === 'Escape' && !backdrop.hidden) closeModal();
});

export function confirmDialog({ title, message, sub, confirmLabel = 'Confirmar', danger = true }) {
  return new Promise((resolve) => {
    const wrap = document.createElement('div');
    wrap.innerHTML = `
      <p class="confirm-text">${message}</p>
      ${sub ? `<p class="confirm-sub">${sub}</p>` : ''}
      <div class="form-actions">
        <button type="button" class="btn btn-ghost" data-act="cancel">Cancelar</button>
        <button type="button" class="btn ${danger ? 'btn-danger' : 'btn-primary'}" data-act="ok">${confirmLabel}</button>
      </div>
    `;
    let decided = false;
    wrap.querySelector('[data-act="cancel"]').addEventListener('click', () => {
      decided = true;
      closeModal();
      resolve(false);
    });
    wrap.querySelector('[data-act="ok"]').addEventListener('click', () => {
      decided = true;
      closeModal();
      resolve(true);
    });
    openModal(title, wrap, {
      onClose: () => {
        if (!decided) resolve(false);
      },
    });
  });
}

export function formEl(html) {
  const div = document.createElement('div');
  div.innerHTML = html;
  return div.firstElementChild;
}

export function formError(container, message) {
  let el = container.querySelector('.form-error');
  if (!message) {
    if (el) el.remove();
    return;
  }
  if (!el) {
    el = document.createElement('div');
    el.className = 'form-error';
    container.prepend(el);
  }
  el.textContent = message;
}

export function fieldErrorsToText(fieldErrors) {
  return Object.entries(fieldErrors)
    .map(([campo, msg]) => `${campo}: ${msg}`)
    .join(' · ');
}

export function money(value) {
  const n = Number(value || 0);
  return n.toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });
}

export function dateTime(value) {
  if (!value) return '—';
  const d = new Date(value);
  if (isNaN(d.getTime())) return String(value);
  return d.toLocaleString('es-CO', { dateStyle: 'medium', timeStyle: 'short' });
}

export function escapeHtml(str) {
  return String(str ?? '').replace(/[&<>"']/g, (c) => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;',
  }[c]));
}

export function setBusy(button, busy) {
  if (!button) return;
  const label = button.querySelector('.btn-label');
  const spinner = button.querySelector('.spinner');
  button.disabled = busy;
  if (spinner) spinner.hidden = !busy;
  if (label && button.dataset.idleLabel === undefined) {
    button.dataset.idleLabel = label.textContent;
  }
}
