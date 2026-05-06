import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CarritoService } from '../../services/carrito.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  menuOpen = false;
  userMenuOpen = false;

  constructor(
    public authService: AuthService,
    public carritoService: CarritoService,
    private router: Router
  ) {}

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  toggleUserMenu(): void {
    this.userMenuOpen = !this.userMenuOpen;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.nav-user-area')) {
      this.userMenuOpen = false;
    }
  }

  onSearch(event: Event): void {
    const input = event.target as HTMLInputElement;
    const query = input.value.trim();
    if (query) {
      this.router.navigate(['/productos'], { queryParams: { buscar: query } });
    }
  }

  logout(): void {
    this.authService.logout();
    this.carritoService.vaciarCarrito();
    this.router.navigate(['/login']);
    this.menuOpen = false;
    this.userMenuOpen = false;
  }
}
