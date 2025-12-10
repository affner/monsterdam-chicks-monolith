import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MoneyEarning from './money-earning';
import MoneyEarningDetail from './money-earning-detail';
import MoneyEarningUpdate from './money-earning-update';
import MoneyEarningDeleteDialog from './money-earning-delete-dialog';

const MoneyEarningRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MoneyEarning />} />
    <Route path="new" element={<MoneyEarningUpdate />} />
    <Route path=":id">
      <Route index element={<MoneyEarningDetail />} />
      <Route path="edit" element={<MoneyEarningUpdate />} />
      <Route path="delete" element={<MoneyEarningDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MoneyEarningRoutes;
