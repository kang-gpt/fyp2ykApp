import React from 'react';

import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserManagement from './user-management';
import Logs from './logs/logs';
import Health from './health/health';
import Metrics from './metrics/metrics';
import Configuration from './configuration/configuration';
import Docs from './docs/docs';
import AdminHomepage from './administration';
import ManageCourtAvailability from './manage-court-availability/ManageCourtAvailability';
import ManageVoucher from './manage-voucher/ManageVoucher';
import ManageBookings from './manage-bookings/ManageBookings';
import ViewRevenue from './view-revenue/ViewRevenue';
import AdminPaymentManagement from 'app/entities/payment-management/payment-management';

const AdministrationRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route index element={<AdminHomepage />} />
      <Route path="user-management/*" element={<UserManagement />} />
      <Route path="health" element={<Health />} />
      <Route path="metrics" element={<Metrics />} />
      <Route path="configuration" element={<Configuration />} />
      <Route path="logs" element={<Logs />} />
      <Route path="docs" element={<Docs />} />
      <Route path="court-availability" element={<ManageCourtAvailability />} />
      <Route path="manage-voucher" element={<ManageVoucher />} />
      <Route path="manage-bookings" element={<ManageBookings />} />
      <Route path="revenue" element={<ViewRevenue />} />
      <Route path="payment-management" element={<AdminPaymentManagement />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default AdministrationRoutes;
