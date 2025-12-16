import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PaymentProviderEvent from './payment-provider-event';
import PaymentProviderEventDetail from './payment-provider-event-detail';
import PaymentProviderEventUpdate from './payment-provider-event-update';
import PaymentProviderEventDeleteDialog from './payment-provider-event-delete-dialog';

const PaymentProviderEventRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PaymentProviderEvent />} />
    <Route path="new" element={<PaymentProviderEventUpdate />} />
    <Route path=":id">
      <Route index element={<PaymentProviderEventDetail />} />
      <Route path="edit" element={<PaymentProviderEventUpdate />} />
      <Route path="delete" element={<PaymentProviderEventDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PaymentProviderEventRoutes;
