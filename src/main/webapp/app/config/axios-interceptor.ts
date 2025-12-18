import axios, { type AxiosError } from 'axios';
import { Storage } from 'react-jhipster';

const TIMEOUT = 1 * 60 * 1000;
axios.defaults.timeout = TIMEOUT;
axios.defaults.baseURL = SERVER_API_URL;

const setupAxiosInterceptors = onUnauthenticated => {
  const logicalPaths = new Set<string>([
    'admin-email-configs',
    'admin-system-configs',
    'auctions',
    'book-marks',
    'chat-rooms',
    'comment-mentions',
    'content-packages',
    'countries',
    'direct-messages',
    'feedbacks',
    'hash-tags',
    'help-categories',
    'help-questions',
    'help-related-articles',
    'help-subcategories',
    'ledger-entries',
    'like-marks',
    'money-earnings',
    'money-gifts',
    'money-withdraws',
    'payment-methods',
    'payment-provider-events',
    'payment-providers',
    'personal-social-links',
    'platform-admin-users',
    'post-comments',
    'post-feeds',
    'post-mentions',
    'post-polls',
    'purchased-contents',
    'purchased-subscriptions',
    'refund-transactions',
    'single-audios',
    'single-photos',
    'single-videos',
    'social-networks',
    'special-awards',
    'states',
    'subscription-bundles',
    'subscription-plan-offers',
    'tax-infos',
    'trial-links',
    'user-associations',
    'user-events',
    'user-lites',
    'user-profiles',
    'user-reports',
    'user-settings',
    'video-stories',
    'viewer-wallets',
  ]);

  const rewriteToLogicalEndpoint = config => {
    const method = (config.method || '').toLowerCase();
    if (!['get', 'delete'].includes(method)) {
      return config;
    }
    const currentUrl: string = config.url || '';
    if (!currentUrl.startsWith('api/') || currentUrl.startsWith('api/logical/')) {
      return config;
    }
    if (config.headers && config.headers['X-Use-Physical'] === 'true') {
      return config;
    }
    const pathWithoutPrefix = currentUrl.substring('api/'.length);
    const targetSegment = pathWithoutPrefix.split(/[/?]/)[0];
    if (logicalPaths.has(targetSegment)) {
      config.url = `api/logical/${pathWithoutPrefix}`;
    }
    return config;
  };

  const onRequestSuccess = config => {
    const token = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    rewriteToLogicalEndpoint(config);
    return config;
  };
  const onResponseSuccess = response => response;
  const onResponseError = (err: AxiosError) => {
    const status = err.status || (err.response ? err.response.status : 0);
    if (status === 401) {
      onUnauthenticated();
    }
    return Promise.reject(err);
  };
  axios.interceptors.request.use(onRequestSuccess);
  axios.interceptors.response.use(onResponseSuccess, onResponseError);
};

export default setupAxiosInterceptors;
