console.log("OKKKK");

const ROLE_CONFIG = {
  itemSelector: ".row-item",

  activeClass: ["bg-blue-50", "border-blue-200"],
  inactiveClass: ["border-transparent", "hover:bg-gray-50"],

  activeTextName: ["text-blue-700"],
  inactiveTextName: ["text-slate-700"],

  activeTextId: ["text-blue-600/70"],
  inactiveTextId: ["text-slate-400"],

  disabledClass: "opacity-50",
};

function setActive(el) {
  el.classList.remove(...ROLE_CONFIG.inactiveClass);
  el.classList.add(...ROLE_CONFIG.activeClass);

  const name = el.querySelector(".role-item-name");
  if (name) {
    name.classList.remove(...ROLE_CONFIG.inactiveTextName);
    name.classList.add(...ROLE_CONFIG.activeTextName);
  }

  const id = el.querySelector(".role-item-id");
  if (id) {
    id.classList.remove(...ROLE_CONFIG.inactiveTextId);
    id.classList.add(...ROLE_CONFIG.activeTextId);
  }

  const icon = el.querySelector(".role-icon");
  if (icon) icon.classList.remove("hidden");
}

function setInactive(el) {
  el.classList.remove(...ROLE_CONFIG.activeClass);
  el.classList.add(...ROLE_CONFIG.inactiveClass);

  const name = el.querySelector(".role-item-name");
  if (name) {
    name.classList.remove(...ROLE_CONFIG.activeTextName);
    name.classList.add(...ROLE_CONFIG.inactiveTextName);
  }

  const id = el.querySelector(".role-item-id");
  if (id) {
    id.classList.remove(...ROLE_CONFIG.activeTextId);
    id.classList.add(...ROLE_CONFIG.inactiveTextId);
  }

  const icon = el.querySelector(".role-icon");
  if (icon) icon.classList.add("hidden");
}

function setCurrentRole(role) {
  state.currentRole = {
    id: role.id,
    name: role.name,
  };
  console.log("=========Current Role=======");
  console.log(state.currentRole);

  console.log("load data permission item" + state.currentRole.name);
  state.serverDataPermission = data[state.currentRole.id - 1];
  console.log("re-render perrmission Item: " + state.currentRole.name);
  initPermissionAccordion();
}

function initRoleList() {
  const container = document.getElementById("role-list");

  const items = container.querySelectorAll(ROLE_CONFIG.itemSelector);
  items.forEach(setInactive);

  if (items.length > 0) {
    setActive(items[0]);
  }

  container.addEventListener("click", (e) => {
    const item = e.target.closest(ROLE_CONFIG.itemSelector);
    if (!item) return;

    const items = container.querySelectorAll(ROLE_CONFIG.itemSelector);
    items.forEach(setInactive);

    setActive(item);
  });
}
function initRoleModal() {
  const roleModal = document.getElementById("roleModal");
  const roleForm = roleModal.querySelector("form");
  const createBtn = document.getElementById("createNewRole");
  const closeRoleModal = document.getElementById("closeRoleModal");

  // open
  const openModal = () => {
    roleModal.classList.remove("hidden");
    document.body.style.overflow = "hidden";
  };

  // close
  const closeModal = () => {
    roleModal.classList.add("hidden");
    document.body.style.overflow = "auto";
  };

  // open button
  createBtn.addEventListener("click", openModal);
  closeRoleModal.addEventListener("click", closeModal);

  // submit form
  roleForm.addEventListener("submit", (e) => {
    e.preventDefault();

    const inputs = roleForm.querySelectorAll("input[type='text']");
    const roleName = inputs[0]?.value.trim();

    if (!roleName) {
      alert("Vui lòng nhập đầy đủ thông tin!");
      return;
    }

    const payload = {
      name: roleName,
    };

    console.log("Tạo role:", payload);

    alert("Tạo vai trò thành công!");
    roleForm.reset();
    closeModal();
  });
}

function toggleAccordion(header) {
  const group = header.parentElement;
  const content = group.querySelector(".accordion-content");
  const icon = header.querySelector(".fa-chevron-down");

  // Đóng tất cả các cái khác (nếu muốn dạng Single Open)
  // document.querySelectorAll('.accordion-content').forEach(el => {
  //     if (el !== content) { el.style.maxHeight = null; el.parentElement.querySelector('.fa-chevron-down').style.transform = 'rotate(0deg)'; }
  // });

  if (content.style.maxHeight) {
    content.style.maxHeight = null;
    icon.style.transform = "rotate(0deg)";
  } else {
    content.style.maxHeight = content.scrollHeight + "px";
    icon.style.transform = "rotate(180deg)";
  }
}

const data1 = {
  role: { id: 1, name: "ADMIN" },
  permissions: [
    {
      module: "USER",
      permissions: [
        { id: 1, action: "USER_READ", description: "Xem danh sách", assigned: true, isActive: 1 },
        { id: 2, action: "USER_CREATE", description: "Thêm user", assigned: false, isActive: 1 },
        { id: 3, action: "USER_DELETE", description: "Xóa user", assigned: true, isActive: 1 },
      ],
    },
    {
      module: "JOB",
      permissions: [
        { id: 4, action: "JOB_READ", description: "Xem tin", assigned: true, isActive: 0 },
        { id: 5, action: "JOB_CREATE", description: "Tạo tin", assigned: false, isActive: 0 },
      ],
    },
    {
      module: "USER",
      permissions: [
        { id: 1, action: "USER_READ", description: "Xem danh sách", assigned: true, isActive: 1 },
        { id: 2, action: "USER_CREATE", description: "Thêm user", assigned: false, isActive: 1 },
        { id: 3, action: "USER_DELETE", description: "Xóa user", assigned: true, isActive: 1 },
      ],
    },
    {
      module: "USER",
      permissions: [
        { id: 1, action: "USER_READ", description: "Xem danh sách", assigned: true, isActive: 1 },
        { id: 2, action: "USER_CREATE", description: "Thêm user", assigned: false, isActive: 1 },
        { id: 3, action: "USER_DELETE", description: "Xóa user", assigned: true, isActive: 1 },
      ],
    },
    {
      module: "USER",
      permissions: [
        { id: 1, action: "USER_READ", description: "Xem danh sách", assigned: true, isActive: 1 },
        { id: 2, action: "USER_CREATE", description: "Thêm user", assigned: false, isActive: 1 },
        { id: 3, action: "USER_DELETE", description: "Xóa user", assigned: true, isActive: 1 },
      ],
    },
  ],
};

const data2 = {
  role: { id: 1, name: "ADMIN" },
  permissions: [
    {
      module: "USER",
      permissions: [
        { id: 1, action: "USER_READ", description: "Xem danh sách", assigned: true, isActive: 1 },
        { id: 2, action: "USER_CREATE", description: "Thêm user", assigned: false, isActive: 1 },
        { id: 3, action: "USER_DELETE", description: "Xóa user", assigned: true, isActive: 1 },
      ],
    },
    {
      module: "JOB",
      permissions: [
        { id: 4, action: "JOB_READ", description: "Xem tin", assigned: true, isActive: 1 },
        { id: 5, action: "JOB_CREATE", description: "Tạo tin", assigned: false, isActive: 1 },
      ],
    },
  ],
};

const data3 = {
  role: { id: 1, name: "ADMIN" },
  permissions: [
    {
      module: "USER",
      permissions: [
        { id: 1, action: "USER_READ", description: "Xem danh sách", assigned: false, isActive: 1 },
        { id: 2, action: "USER_CREATE", description: "Thêm user", assigned: false, isActive: 1 },
        { id: 3, action: "USER_DELETE", description: "Xóa user", assigned: false, isActive: 1 },
      ],
    },
    {
      module: "JOB",
      permissions: [
        { id: 4, action: "JOB_READ", description: "Xem tin", assigned: false, isActive: 1 },
        { id: 5, action: "JOB_CREATE", description: "Tạo tin", assigned: false, isActive: 1 },
      ],
    },
  ],
};

const data = [data1, data2, data3];

function renderPermissionItemUI(permission) {
  const active = Number(permission.isActive) === 1;
  return `
    <label
        class="perm-item flex items-center p-1.5 rounded  group transition
        ${active ? "hover:bg-blue-50 cursor-pointer" : "pointer-events-none bg-gray-50 border-gray-200 opacity-60"} "
    >
        <input type="checkbox" 
          ${isChecked(permission.id, state.currentRole.id, permission.assigned) ? "checked" : ""}
          class="w-3.5 h-3.5 accent-blue-600 shrink-0"
          onchange="togglePermission(this, ${permission.id}, ${permission.assigned})"
        >
        <div class="ml-2 overflow-hidden">
        <p class="text-[11px] font-bold text-slate-700 truncate">${permission.action}</p>
        <p class="text-[9px] text-gray-400 truncate leading-tight">${permission.description}</p>
        </div>
    </label>
    `;
}

function renderPermissionUI(module, permissions) {
  return `
    <div class="module-group border border-gray-200 rounded-lg overflow-hidden shadow-sm">

        <div onclick="toggleAccordion(this)"
            class="flex items-center justify-between px-3 py-2 bg-slate-50 cursor-pointer hover:bg-slate-100 transition">

            <div class="flex items-center space-x-2">
                <i class="fas fa-shield-alt text-slate-400 text-xs"></i>
                <span class="text-[11px] font-bold text-slate-700 uppercase tracking-tight">
                    Nhóm ${module}
                </span>
            </div>

            <i class="fas fa-chevron-down text-[10px] text-gray-400"></i>
        </div>

        <div class="accordion-content max-h-0 overflow-hidden transition-all bg-white">
            <div class="p-2 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-1 border-t border-gray-100">
                ${permissions.map(renderPermissionItemUI).join("")}
            </div>
        </div>

    </div>
    `;
}

/**
 * STATE STRUCTURE
 *
 * currentRole:
 *   - id: number | null
 *   - name: string | null
 *
 * currentChangePermission:
 *   [roleId]:
 *     - added: Set<number>
 *     - removed: Set<number>
 */

const state = {
  currentRole: {
    id: null,
    name: null,
  },

  serverDataPermission: {},

  currentChangePermission: {},

  filters: {
    keyword: "",
    type: "all",
    status: "",
    module: "",
  },
};

function togglePermission(e, permId, assigned) {
  const checked = e.checked;

  const roleId = state.currentRole.id;

  if (!state.currentChangePermission[roleId]) {
    state.currentChangePermission[roleId] = {
      added: new Set(),
      removed: new Set(),
    };
  }

  const draft = state.currentChangePermission[roleId];

  if (checked === assigned) {
    draft.added.delete(permId);
    draft.removed.delete(permId);
  } else if (checked) {
    draft.added.add(permId);
    draft.removed.delete(permId);
  } else {
    draft.added.delete(permId);
    draft.removed.add(permId);
  }

  console.log("=====Permission Change=======");
  console.log(state.currentChangePermission);
}

function isChecked(permId, roleId, assigned) {
  draft = state.currentChangePermission[roleId];
  if (!draft) return assigned;

  if (draft.added.has(permId)) return true;
  if (draft.removed.has(permId)) return false;

  return assigned;
}

function initPermissionAccordion() {
  const containter = document.getElementById("permission-accordion");
  containter.innerHTML = state.serverDataPermission.permissions
    .map((obj) => renderPermissionUI(obj.module, obj.permissions))
    .join("");
}
function initSearchEvent() {
  const searchInput = document.getElementById("searchInput");
  if (searchInput) {
    searchInput.addEventListener("input", (e) => {
      state.filters.keyword = e.target.value.toLowerCase();
      applyFilter();
    });
  }

  document.getElementById("searchType")?.addEventListener("change", (e) => {
    state.filters.type = e.target.value;
    applyFilter();
  });

  document.getElementById("searchActive")?.addEventListener("change", (e) => {
    state.filters.status = e.target.value;
    applyFilter();
  });

  document.getElementById("searchModule")?.addEventListener("change", (e) => {
    state.filters.module = e.target.value;
    applyFilter();
  });
}

function applyFilter() {
  const keyword = state.filters.keyword;
  const type = state.filters.type;

  let dataSource = state.serverDataPermission;

  let filtered = dataSource.permissions.map(group => {
    let perms = group.permissions;

    // filter keyword
    if (keyword) {
      perms = perms.filter(p =>
        p.action.toLowerCase().includes(keyword) ||
        p.description.toLowerCase().includes(keyword)
      );
    }

    // filter status
    if (state.filters.status === "active") {
      perms = perms.filter(p => Number(p.isActive) === 1);
    }

    if (state.filters.status === "inactive") {
      perms = perms.filter(p => Number(p.isActive) === 0);
    }

    return {
      ...group,
      permissions: perms
    };
  });

  // remove empty group
  filtered = filtered.filter(g => g.permissions.length > 0);

  // update state
  state.serverDataPermission = {
    ...dataSource,
    permissions: filtered
  };

  initPermissionAccordion();
}

document.addEventListener("DOMContentLoaded", () => {
  initRoleList();
  initRoleModal();
  state.serverDataPermission = data1;
  initPermissionAccordion();
  state.currentRole = { ...data.role };
  initSearchEvent()
});
