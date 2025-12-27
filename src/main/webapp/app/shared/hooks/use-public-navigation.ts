import { useEffect, useState } from 'react';
import axios from 'axios';

export interface PublicNavigationItem {
  id?: string;
  label: string;
  path: string;
  description?: string;
  requiresAuth?: boolean;
  roles?: string[];
}

interface PublicNavigationResponse {
  items?: PublicNavigationItem[];
}

const NAVIGATION_ENDPOINT = 'api/bff/navigation';

const fallbackNavigation: PublicNavigationItem[] = [
  {
    id: 'explore',
    label: 'Explorar',
    path: '/public/explorar',
    description: 'Descubre contenido disponible para visitantes y viewers.',
  },
  {
    id: 'models',
    label: 'Modelos',
    path: '/public/modelos',
    description: 'Navega perfiles y contenido destacado.',
  },
  {
    id: 'help',
    label: 'Ayuda',
    path: '/public/ayuda',
    description: 'Preguntas frecuentes y soporte inicial.',
  },
];

let cachedNavigation: PublicNavigationItem[] | null = null;
let cachedPromise: Promise<PublicNavigationItem[]> | null = null;

const resolveNavigation = (data: PublicNavigationItem[] | PublicNavigationResponse) => {
  if (Array.isArray(data)) {
    return data;
  }
  if (data?.items && Array.isArray(data.items)) {
    return data.items;
  }
  return [];
};

export const usePublicNavigation = () => {
  const [items, setItems] = useState<PublicNavigationItem[]>(cachedNavigation ?? fallbackNavigation);
  const [loading, setLoading] = useState(!cachedNavigation);
  const [hasError, setHasError] = useState(false);

  useEffect(() => {
    let isMounted = true;

    const loadNavigation = async () => {
      try {
        if (!cachedPromise) {
          cachedPromise = axios.get<PublicNavigationItem[] | PublicNavigationResponse>(NAVIGATION_ENDPOINT).then(response => {
            const resolved = resolveNavigation(response.data);
            return resolved.length ? resolved : fallbackNavigation;
          });
        }
        const data = await cachedPromise;
        cachedNavigation = data;
        if (isMounted) {
          setItems(data);
          setHasError(false);
          setLoading(false);
        }
      } catch (error) {
        if (isMounted) {
          setItems(fallbackNavigation);
          setHasError(true);
          setLoading(false);
        }
      }
    };

    loadNavigation();

    return () => {
      isMounted = false;
    };
  }, []);

  return {
    items,
    loading,
    hasError,
  };
};
