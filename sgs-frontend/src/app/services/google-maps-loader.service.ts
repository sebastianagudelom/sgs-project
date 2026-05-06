import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

declare global {
  interface Window {
    google?: any;
    initSgsGoogleMaps?: () => void;
  }
}

@Injectable({ providedIn: 'root' })
export class GoogleMapsLoaderService {

  private loadingPromise: Promise<void> | null = null;

  load(): Promise<void> {
    if (window.google?.maps) {
      return Promise.resolve();
    }

    if (this.loadingPromise) {
      return this.loadingPromise;
    }

    if (!environment.googleMapsApiKey) {
      return Promise.reject(new Error('Configura googleMapsApiKey en environments/environment.ts'));
    }

    this.loadingPromise = new Promise<void>((resolve, reject) => {
      const callbackName = 'initSgsGoogleMaps';

      window.initSgsGoogleMaps = () => {
        resolve();
      };

      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${encodeURIComponent(environment.googleMapsApiKey)}&loading=async&callback=${callbackName}`;
      script.async = true;
      script.defer = true;
      script.dataset['googleMaps'] = 'sgs';
      script.onerror = () => reject(new Error('No fue posible cargar Google Maps'));

      document.head.appendChild(script);
    });

    return this.loadingPromise;
  }
}
