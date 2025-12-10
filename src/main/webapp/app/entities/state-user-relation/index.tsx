import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StateUserRelation from './state-user-relation';
import StateUserRelationDetail from './state-user-relation-detail';
import StateUserRelationUpdate from './state-user-relation-update';
import StateUserRelationDeleteDialog from './state-user-relation-delete-dialog';

const StateUserRelationRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StateUserRelation />} />
    <Route path="new" element={<StateUserRelationUpdate />} />
    <Route path=":id">
      <Route index element={<StateUserRelationDetail />} />
      <Route path="edit" element={<StateUserRelationUpdate />} />
      <Route path="delete" element={<StateUserRelationDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StateUserRelationRoutes;
