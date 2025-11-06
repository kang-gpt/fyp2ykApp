import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ClientTier from './client-tier';
import ClientTierDetail from './client-tier-detail';
import ClientTierUpdate from './client-tier-update';
import ClientTierDeleteDialog from './client-tier-delete-dialog';

const ClientTierRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ClientTier />} />
    <Route path="new" element={<ClientTierUpdate />} />
    <Route path=":id">
      <Route index element={<ClientTierDetail />} />
      <Route path="edit" element={<ClientTierUpdate />} />
      <Route path="delete" element={<ClientTierDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ClientTierRoutes;
