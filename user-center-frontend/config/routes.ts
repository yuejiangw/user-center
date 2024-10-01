export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {name: 'Login', path: '/user/login', component: './User/Login'},
      {name: 'Register', path: '/user/register', component: './User/Register'},
    ],
  },
  {name: 'Home', path: '/welcome', icon: 'dashboard', component: './Welcome'},
  {
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    name: 'Admin',
    routes: [
      {name: 'Admin Home', path: '/admin/home', component: './Admin/Admin'},
      {name: 'User Management', path: '/admin/user-manage', component: './Admin/UserManage/UserManageForm'},
      {name: 'Plan Management', path: '/admin/plan-manage', component: './Admin/PlanManage'},
    ],
  },
  {name: 'Profile', icon: 'profile', path: '/user/profile', component: './User/Profile'},
  {path: '/', redirect: '/welcome'},
  {path: '*', layout: false, component: './404'},
];
