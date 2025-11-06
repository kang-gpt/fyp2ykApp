import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SportCourts from './sport/sport-courts';

import Sport from './sport';
import Court from './court';
import ClientTier from './client-tier';
import Client from './client';
import Payment from './payment';
import Booking from './booking';
import TimeSlot from './time-slot';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="sport/*" element={<Sport />} />
        <Route path="court/*" element={<Court />} />
        <Route path="client-tier/*" element={<ClientTier />} />
        <Route path="client/*" element={<Client />} />
        <Route path="payment/*" element={<Payment />} />
        <Route path="sport/:sportId/courts" element={<SportCourts />} />
        <Route path="booking/*" element={<Booking />} />
        <Route path="time-slot/*" element={<TimeSlot />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
