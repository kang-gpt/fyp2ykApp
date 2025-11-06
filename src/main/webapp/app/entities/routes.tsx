import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SportCourts from './sport/sport-courts';

import Booking from './booking';
import TimeSlot from './time-slot';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="sport/:sportId/courts" element={<SportCourts />} />
        <Route path="booking/*" element={<Booking />} />
        <Route path="time-slot/*" element={<TimeSlot />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
