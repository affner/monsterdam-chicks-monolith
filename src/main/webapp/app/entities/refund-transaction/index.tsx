import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import RefundTransaction from './refund-transaction';
import RefundTransactionDetail from './refund-transaction-detail';
import RefundTransactionUpdate from './refund-transaction-update';
import RefundTransactionDeleteDialog from './refund-transaction-delete-dialog';

const RefundTransactionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<RefundTransaction />} />
    <Route path="new" element={<RefundTransactionUpdate />} />
    <Route path=":id">
      <Route index element={<RefundTransactionDetail />} />
      <Route path="edit" element={<RefundTransactionUpdate />} />
      <Route path="delete" element={<RefundTransactionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RefundTransactionRoutes;
