import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { GoogleMapsLoaderService } from '../../services/google-maps-loader.service';

@Component({
  selector: 'app-address-map',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './address-map.component.html',
  styleUrl: './address-map.component.css'
})
export class AddressMapComponent implements AfterViewInit, OnChanges, OnDestroy {
  @Input() direccion = '';
  @Input() latitud: number | null = null;
  @Input() longitud: number | null = null;

  @Output() direccionChange = new EventEmitter<string>();
  @Output() latitudChange = new EventEmitter<number | null>();
  @Output() longitudChange = new EventEmitter<number | null>();

  @ViewChild('mapContainer') mapContainer!: ElementRef<HTMLDivElement>;
  @ViewChild('direccionInput') direccionInput!: ElementRef<HTMLInputElement>;

  loadingMap = false;
  geocoding = false;
  errorMessage = '';

  private map: any;
  private marker: any;
  private geocoder: any;
  private geocodeTimeout: ReturnType<typeof setTimeout> | null = null;
  private readonly defaultCenter = { lat: 4.5339, lng: -75.6811 };

  constructor(private googleMapsLoader: GoogleMapsLoaderService) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.inicializarMapa());
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.map) {
      return;
    }

    if ((changes['latitud'] || changes['longitud']) && this.tieneCoordenadas()) {
      queueMicrotask(() => this.posicionarMarcador({ lat: this.latitud!, lng: this.longitud! }, false));
      return;
    }

    if (changes['direccion'] && !changes['direccion'].firstChange) {
      this.programarGeocodificacion();
    }
  }

  ngOnDestroy(): void {
    if (this.geocodeTimeout) {
      clearTimeout(this.geocodeTimeout);
    }
  }

  onDireccionInput(value: string): void {
    this.direccion = value;
    this.direccionChange.emit(value);
    this.programarGeocodificacion();
  }

  usarMarcadorActual(): void {
    if (!this.marker) {
      return;
    }

    const position = this.marker.getPosition();
    this.emitirCoordenadas(position.lat(), position.lng());
    this.reverseGeocode(position);
  }

  private inicializarMapa(): void {
    this.loadingMap = true;
    this.errorMessage = '';

    this.googleMapsLoader.load()
      .then(() => {
        const google = window.google!;
        const center = this.tieneCoordenadas()
          ? { lat: this.latitud!, lng: this.longitud! }
          : this.defaultCenter;

        this.geocoder = new google.maps.Geocoder();
        this.map = new google.maps.Map(this.mapContainer.nativeElement, {
          center,
          zoom: this.tieneCoordenadas() ? 16 : 13,
          mapTypeControl: false,
          streetViewControl: false,
          fullscreenControl: false
        });

        this.marker = new google.maps.Marker({
          map: this.map,
          position: center,
          draggable: true
        });

        this.marker.addListener('dragend', () => {
          const position = this.marker.getPosition();
          this.emitirCoordenadas(position.lat(), position.lng());
          this.reverseGeocode(position);
        });

        if (!this.tieneCoordenadas() && this.direccion.trim().length >= 8) {
          this.programarGeocodificacion(100);
        }

        this.loadingMap = false;
      })
      .catch((error: Error) => {
        this.loadingMap = false;
        this.errorMessage = error.message;
      });
  }

  private programarGeocodificacion(delay = 650): void {
    if (!this.geocoder || this.direccion.trim().length < 8) {
      return;
    }

    if (this.geocodeTimeout) {
      clearTimeout(this.geocodeTimeout);
    }

    this.geocodeTimeout = setTimeout(() => this.geocodificarDireccion(), delay);
  }

  private geocodificarDireccion(): void {
    this.geocoding = true;
    this.errorMessage = '';

    this.geocoder.geocode(
      {
        address: this.direccion,
        componentRestrictions: { country: 'CO' }
      },
      (results: any[], status: string) => {
        this.geocoding = false;

        if (status !== 'OK' || !results?.length) {
          this.errorMessage = 'No se encontraron coordenadas para esta direccion';
          return;
        }

        const location = results[0].geometry.location;
        this.posicionarMarcador(location, true);
      }
    );
  }

  private reverseGeocode(position: any): void {
    if (!this.geocoder) {
      return;
    }

    this.geocoder.geocode({ location: position }, (results: any[], status: string) => {
      if (status === 'OK' && results?.length) {
        this.direccion = results[0].formatted_address;
        this.direccionChange.emit(this.direccion);
      }
    });
  }

  private posicionarMarcador(position: any, emitir: boolean): void {
    const lat = typeof position.lat === 'function' ? position.lat() : position.lat;
    const lng = typeof position.lng === 'function' ? position.lng() : position.lng;
    const literal = { lat, lng };

    this.marker.setPosition(literal);
    this.map.setCenter(literal);
    this.map.setZoom(16);

    if (emitir) {
      this.emitirCoordenadas(lat, lng);
    }
  }

  private emitirCoordenadas(lat: number, lng: number): void {
    this.latitud = Number(lat.toFixed(7));
    this.longitud = Number(lng.toFixed(7));
    this.latitudChange.emit(this.latitud);
    this.longitudChange.emit(this.longitud);
  }

  private tieneCoordenadas(): boolean {
    return this.latitud !== null && this.longitud !== null;
  }
}
