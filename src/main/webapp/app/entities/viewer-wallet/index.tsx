import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ViewerWallet from './viewer-wallet';
import ViewerWalletDetail from './viewer-wallet-detail';
import ViewerWalletUpdate from './viewer-wallet-update';
import ViewerWalletDeleteDialog from './viewer-wallet-delete-dialog';

const ViewerWalletRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ViewerWallet />} />
    <Route path="new" element={<ViewerWalletUpdate />} />
    <Route path=":id">
      <Route index element={<ViewerWalletDetail />} />
      <Route path="edit" element={<ViewerWalletUpdate />} />
      <Route path="delete" element={<ViewerWalletDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ViewerWalletRoutes;
