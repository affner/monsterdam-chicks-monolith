import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MoneyGift from './money-gift';
import MoneyGiftDetail from './money-gift-detail';
import MoneyGiftUpdate from './money-gift-update';
import MoneyGiftDeleteDialog from './money-gift-delete-dialog';

const MoneyGiftRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MoneyGift />} />
    <Route path="new" element={<MoneyGiftUpdate />} />
    <Route path=":id">
      <Route index element={<MoneyGiftDetail />} />
      <Route path="edit" element={<MoneyGiftUpdate />} />
      <Route path="delete" element={<MoneyGiftDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MoneyGiftRoutes;
