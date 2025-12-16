import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MoneyWithdraw from './money-withdraw';
import MoneyWithdrawDetail from './money-withdraw-detail';
import MoneyWithdrawUpdate from './money-withdraw-update';
import MoneyWithdrawDeleteDialog from './money-withdraw-delete-dialog';

const MoneyWithdrawRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MoneyWithdraw />} />
    <Route path="new" element={<MoneyWithdrawUpdate />} />
    <Route path=":id">
      <Route index element={<MoneyWithdrawDetail />} />
      <Route path="edit" element={<MoneyWithdrawUpdate />} />
      <Route path="delete" element={<MoneyWithdrawDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MoneyWithdrawRoutes;
