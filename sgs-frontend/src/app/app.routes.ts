import { Routes } from '@angular/router';
import { authGuard, adminGuard, guestGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent),
    canActivate: [guestGuard]
  },
  {
    path: 'registro',
    loadComponent: () => import('./pages/registro/registro.component').then(m => m.RegistroComponent),
    canActivate: [guestGuard]
  },
  {
    path: 'verificar',
    loadComponent: () => import('./pages/verificar/verificar.component').then(m => m.VerificarComponent)
  },
  {
    path: 'productos',
    loadComponent: () => import('./pages/producto-lista/producto-lista.component').then(m => m.ProductoListaComponent)
  },
  {
    path: 'productos/nuevo',
    loadComponent: () => import('./pages/producto-form/producto-form.component').then(m => m.ProductoFormComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'productos/editar/:id',
    loadComponent: () => import('./pages/producto-form/producto-form.component').then(m => m.ProductoFormComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'productos/:id',
    loadComponent: () => import('./pages/producto-detalle/producto-detalle.component').then(m => m.ProductoDetalleComponent)
  },
  {
    path: 'categorias',
    loadComponent: () => import('./pages/categorias/categorias.component').then(m => m.CategoriasComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'carrito',
    loadComponent: () => import('./pages/carrito/carrito.component').then(m => m.CarritoComponent),
    canActivate: [authGuard]
  },
  {
    path: 'mis-pedidos',
    loadComponent: () => import('./pages/mis-pedidos/mis-pedidos.component').then(m => m.MisPedidosComponent),
    canActivate: [authGuard]
  },
  {
    path: 'mi-perfil',
    loadComponent: () => import('./pages/perfil/perfil.component').then(m => m.PerfilComponent),
    canActivate: [authGuard]
  },
  {
    path: 'admin/pedidos',
    loadComponent: () => import('./pages/admin-pedidos/admin-pedidos.component').then(m => m.AdminPedidosComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'admin/clientes',
    loadComponent: () => import('./pages/admin-clientes/admin-clientes.component').then(m => m.AdminClientesComponent),
    canActivate: [adminGuard]
  },
  {
    path: 'admin/inventario-alertas',
    loadComponent: () => import('./pages/admin-inventario-alertas/admin-inventario-alertas.component').then(m => m.AdminInventarioAlertasComponent),
    canActivate: [adminGuard]
  },
  {
    path: '',
    redirectTo: 'productos',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'productos'
  }
];
