import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TimeSlot from './time-slot';
import TimeSlotDetail from './time-slot-detail';
import TimeSlotUpdate from './time-slot-update';
import TimeSlotDeleteDialog from './time-slot-delete-dialog';

const TimeSlotRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TimeSlot />} />
    <Route path="new" element={<TimeSlotUpdate />} />
    <Route path=":id">
      <Route index element={<TimeSlotDetail />} />
      <Route path="edit" element={<TimeSlotUpdate />} />
      <Route path="delete" element={<TimeSlotDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimeSlotRoutes;
