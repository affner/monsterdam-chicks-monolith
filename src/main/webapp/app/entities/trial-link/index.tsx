import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TrialLink from './trial-link';
import TrialLinkDetail from './trial-link-detail';
import TrialLinkUpdate from './trial-link-update';
import TrialLinkDeleteDialog from './trial-link-delete-dialog';

const TrialLinkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TrialLink />} />
    <Route path="new" element={<TrialLinkUpdate />} />
    <Route path=":id">
      <Route index element={<TrialLinkDetail />} />
      <Route path="edit" element={<TrialLinkUpdate />} />
      <Route path="delete" element={<TrialLinkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TrialLinkRoutes;
