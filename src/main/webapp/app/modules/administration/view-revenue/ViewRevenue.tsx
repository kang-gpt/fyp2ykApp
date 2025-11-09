import React, { useState, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import axios from 'axios';

const ViewRevenue = () => {
  const [period, setPeriod] = useState('daily');
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRevenue = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`/api/revenue?period=${period}`);
        setData(response.data);
        setError(null);
      } catch (err) {
        setError('Failed to fetch revenue data.');
        setData([]);
      } finally {
        setLoading(false);
      }
    };

    fetchRevenue();
  }, [period]);

  const totalRevenue = data.reduce((acc, item) => acc + item.revenue, 0);

  return (
    <div>
      <h2>View Revenue</h2>
      <div className="btn-group mb-4" role="group">
        <button type="button" className={`btn ${period === 'daily' ? 'btn-primary' : 'btn-secondary'}`} onClick={() => setPeriod('daily')}>
          Daily
        </button>
        <button
          type="button"
          className={`btn ${period === 'weekly' ? 'btn-primary' : 'btn-secondary'}`}
          onClick={() => setPeriod('weekly')}
        >
          Weekly
        </button>
        <button
          type="button"
          className={`btn ${period === 'monthly' ? 'btn-primary' : 'btn-secondary'}`}
          onClick={() => setPeriod('monthly')}
        >
          Monthly
        </button>
      </div>

      {loading && <p>Loading...</p>}
      {error && <p className="text-danger">{error}</p>}

      <div className="card bg-light p-3 mb-4">
        <h4>Total Revenue ({period})</h4>
        <h3>${totalRevenue.toLocaleString()}</h3>
      </div>

      <ResponsiveContainer width="100%" height={400}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="revenue" fill="#8884d8" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default ViewRevenue;
