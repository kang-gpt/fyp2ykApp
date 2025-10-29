import React, { useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const dailyData = [
  { name: 'Mon', revenue: 4000 },
  { name: 'Tue', revenue: 3000 },
  { name: 'Wed', revenue: 2000 },
  { name: 'Thu', revenue: 2780 },
  { name: 'Fri', revenue: 1890 },
  { name: 'Sat', revenue: 2390 },
  { name: 'Sun', revenue: 3490 },
];

const weeklyData = [
  { name: 'Week 1', revenue: 15000 },
  { name: 'Week 2', revenue: 20000 },
  { name: 'Week 3', revenue: 18000 },
  { name: 'Week 4', revenue: 22000 },
];

const monthlyData = [
  { name: 'Jan', revenue: 80000 },
  { name: 'Feb', revenue: 90000 },
  { name: 'Mar', revenue: 75000 },
  { name: 'Apr', revenue: 110000 },
  { name: 'May', revenue: 95000 },
  { name: 'Jun', revenue: 120000 },
];

const ViewRevenue = () => {
  const [period, setPeriod] = useState('daily');

  const getData = () => {
    switch (period) {
      case 'weekly':
        return weeklyData;
      case 'monthly':
        return monthlyData;
      case 'daily':
      default:
        return dailyData;
    }
  };

  const data = getData();
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
