import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ViewRevenue = () => {
  const [totalRevenue, setTotalRevenue] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTodayRevenue = async () => {
      setLoading(true);
      try {
        const response = await axios.get('/api/revenue/total?period=daily');
        setTotalRevenue(response.data);
        setError(null);
      } catch (err) {
        setError("Failed to fetch today's revenue data.");
        setTotalRevenue(0);
      } finally {
        setLoading(false);
      }
    };

    fetchTodayRevenue();
  }, []);

  return (
    <div className="container-fluid">
      <div className="row">
        <div className="col-md-12">
          <h2 className="mb-4">View Revenue - Daily</h2>

          {loading && <p>Loading...</p>}
          {error && <p className="text-danger">{error}</p>}

          <div className="card bg-light p-4">
            <h4 className="text-muted mb-3">Total Approved Bookings Revenue (Today)</h4>
            <h1 className="display-4 text-success">
              RM {totalRevenue.toLocaleString('en-MY', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </h1>
            <p className="text-muted mt-2">
              {new Date().toLocaleDateString('en-MY', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              })}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewRevenue;
