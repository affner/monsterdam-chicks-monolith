import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubscriptionPlanOffer from './subscription-plan-offer';
import SubscriptionPlanOfferDetail from './subscription-plan-offer-detail';
import SubscriptionPlanOfferUpdate from './subscription-plan-offer-update';
import SubscriptionPlanOfferDeleteDialog from './subscription-plan-offer-delete-dialog';

const SubscriptionPlanOfferRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubscriptionPlanOffer />} />
    <Route path="new" element={<SubscriptionPlanOfferUpdate />} />
    <Route path=":id">
      <Route index element={<SubscriptionPlanOfferDetail />} />
      <Route path="edit" element={<SubscriptionPlanOfferUpdate />} />
      <Route path="delete" element={<SubscriptionPlanOfferDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubscriptionPlanOfferRoutes;
