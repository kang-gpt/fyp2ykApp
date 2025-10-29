import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Sport from './sport';
import SportDetail from './sport-detail';
import SportUpdate from './sport-update';
import SportDeleteDialog from './sport-delete-dialog';

const SportRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Sport />} />
    <Route path="new" element={<SportUpdate />} />
    <Route path=":id">
      <Route index element={<SportDetail />} />
      <Route path="edit" element={<SportUpdate />} />
      <Route path="delete" element={<SportDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SportRoutes;
