import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ItemCarrito } from '../models/pedido.model';

@Injectable({ providedIn: 'root' })
export class CarritoService {

  private readonly STORAGE_KEY = 'carrito';
  private itemsSubject = new BehaviorSubject<ItemCarrito[]>(this.getStoredItems());

  items$ = this.itemsSubject.asObservable();

  get items(): ItemCarrito[] {
    return this.itemsSubject.value;
  }

  get totalItems(): number {
    return this.items.reduce((sum, item) => sum + item.cantidad, 0);
  }

  get totalPrecio(): number {
    return this.items.reduce((sum, item) => sum + item.precio * item.cantidad, 0);
  }

  agregarItem(producto: { id: number; nombre: string; precio: number; imagenUrl: string; stock: number }, cantidad: number = 1): void {
    const items = [...this.items];
    const existente = items.find(i => i.productoId === producto.id);

    if (existente) {
      const nuevaCantidad = existente.cantidad + cantidad;
      if (nuevaCantidad > producto.stock) {
        throw new Error(`Stock insuficiente. Disponible: ${producto.stock}`);
      }
      existente.cantidad = nuevaCantidad;
    } else {
      if (cantidad > producto.stock) {
        throw new Error(`Stock insuficiente. Disponible: ${producto.stock}`);
      }
      items.push({
        productoId: producto.id,
        nombre: producto.nombre,
        precio: producto.precio,
        cantidad,
        imagenUrl: producto.imagenUrl,
        stock: producto.stock
      });
    }

    this.updateItems(items);
  }

  actualizarCantidad(productoId: number, cantidad: number): void {
    const items = [...this.items];
    const item = items.find(i => i.productoId === productoId);
    if (item) {
      if (cantidad > item.stock) {
        throw new Error(`Stock insuficiente. Disponible: ${item.stock}`);
      }
      if (cantidad <= 0) {
        this.eliminarItem(productoId);
        return;
      }
      item.cantidad = cantidad;
      this.updateItems(items);
    }
  }

  eliminarItem(productoId: number): void {
    const items = this.items.filter(i => i.productoId !== productoId);
    this.updateItems(items);
  }

  vaciarCarrito(): void {
    this.updateItems([]);
  }

  private updateItems(items: ItemCarrito[]): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(items));
    this.itemsSubject.next(items);
  }

  private getStoredItems(): ItemCarrito[] {
    const data = localStorage.getItem(this.STORAGE_KEY);
    return data ? JSON.parse(data) : [];
  }
}
