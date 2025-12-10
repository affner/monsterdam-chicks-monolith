import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlatformAdminUser from './platform-admin-user';
import PlatformAdminUserDetail from './platform-admin-user-detail';
import PlatformAdminUserUpdate from './platform-admin-user-update';
import PlatformAdminUserDeleteDialog from './platform-admin-user-delete-dialog';

const PlatformAdminUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlatformAdminUser />} />
    <Route path="new" element={<PlatformAdminUserUpdate />} />
    <Route path=":id">
      <Route index element={<PlatformAdminUserDetail />} />
      <Route path="edit" element={<PlatformAdminUserUpdate />} />
      <Route path="delete" element={<PlatformAdminUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlatformAdminUserRoutes;
