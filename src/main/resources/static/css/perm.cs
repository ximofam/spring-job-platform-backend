/* hover */
.row-item:hover {
  background-color: #f9fafb;
}

/* trạng thái mặc định */
.role-item-name {
  color: #334155; /* slate-700 */
}

.role-item-id {
  color: #94a3b8; /* slate-400 */
}

/* icon mặc định ẩn */
.role-icon {
  display: none;
}

/* ===== ACTIVE STATE ===== */
.row-item.active {
  background-color: #eff6ff;   /* blue-50 */
  border-color: #bfdbfe;       /* blue-200 */
}

.row-item.active .role-item-name {
  color: #1d4ed8; /* blue-700 */
}

.row-item.active .role-item-id {
  color: rgba(37, 99, 235, 0.7);
}

.row-item.active .role-icon {
  display: inline-block;
}