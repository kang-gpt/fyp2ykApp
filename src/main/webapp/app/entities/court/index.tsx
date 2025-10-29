import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { Court } from './court';
import CourtDetail from './court-detail';
import CourtUpdate from './court-update';
import CourtDeleteDialog from './court-delete-dialog';

const CourtRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Court />} />
    <Route path="new" element={<CourtUpdate />} />
    <Route path=":id">
      <Route index element={<CourtDetail />} />
      <Route path="edit" element={<CourtUpdate />} />
      <Route path="delete" element={<CourtDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CourtRoutes;
