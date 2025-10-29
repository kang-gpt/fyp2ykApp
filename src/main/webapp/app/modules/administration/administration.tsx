import React from 'react';
import { Link } from 'react-router-dom';

const AdminHomepage = () => {
  return (
    <div>
      <h1>Admin Homepage</h1>
      <div className="list-group">
        <Link to="/admin/court-availability" className="list-group-item list-group-item-action">
          Manage Court Availability
        </Link>
        <Link to="/admin/manage-voucher" className="list-group-item list-group-item-action">
          Manage Voucher
        </Link>
        <Link to="/admin/manage-bookings" className="list-group-item list-group-item-action">
          Manage Bookings
        </Link>
        <Link to="/admin/revenue" className="list-group-item list-group-item-action">
          View Revenue
        </Link>
        <Link to="/admin/user-management" className="list-group-item list-group-item-action">
          Manage User Information
        </Link>
      </div>
    </div>
  );
};

export default AdminHomepage;
