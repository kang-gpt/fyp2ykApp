import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Button, Table, Input } from 'reactstrap';
import axios from 'axios';

interface IPayment {
  id?: number;
  amount?: number;
  paymentDate?: string;
  status?: string;
  qrCodeUrl?: string;
  transactionId?: string;
  booking?: { id: number };
  user?: { id: number; login: string };
}

const AdminPaymentManagement = () => {
  const [payments, setPayments] = useState<IPayment[]>([]);
  const [filterStatus, setFilterStatus] = useState<string>('All');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const API_URL = '/api/payments';

  const fetchPayments = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<IPayment[]>(API_URL);
      setPayments(response.data);
    } catch (err) {
      setError('Failed to fetch payments.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPayments();
  }, []);

  const handleStatusChange = async (paymentId: number, newStatus: string) => {
    setLoading(true);
    setError(null);
    try {
      await axios.put(`${API_URL}/${paymentId}/status?status=${newStatus}`);
      fetchPayments(); // Refresh the list after update
    } catch (err) {
      setError('Failed to update payment status.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const filteredPayments = payments.filter(payment => {
    if (filterStatus === 'All') {
      return true;
    }
    return payment.status === filterStatus;
  });

  return (
    <div>
      <h2 id="payment-heading" data-cy="PaymentHeading">
        <Translate contentKey="ykApp.payment.home.title">Payment Management</Translate>
      </h2>

      <div className="d-flex justify-content-end mb-3">
        <Input type="select" name="statusFilter" id="statusFilter" onChange={e => setFilterStatus(e.target.value)} value={filterStatus}>
          <option value="All">All</option>
          <option value="Pending">Pending</option>
          <option value="Completed">Completed</option>
          <option value="Failed">Failed</option>
        </Input>
        <Button className="ms-2" color="info" onClick={fetchPayments} disabled={loading}>
          <Translate contentKey="ykApp.payment.home.refreshListLabel">Refresh List</Translate>
        </Button>
      </div>

      {loading && <p>Loading payments...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && filteredPayments.length === 0 && (
        <div className="alert alert-warning">
          <Translate contentKey="ykApp.payment.home.notFound">No Payments found</Translate>
        </div>
      )}

      {!loading && !error && filteredPayments.length > 0 && (
        <Table responsive striped>
          <thead>
            <tr>
              <th>ID</th>
              <th><Translate contentKey="ykApp.payment.amount">Amount</Translate></th>
              <th><Translate contentKey="ykApp.payment.paymentDate">Payment Date</Translate></th>
              <th><Translate contentKey="ykApp.payment.status">Status</Translate></th>
              <th><Translate contentKey="ykApp.payment.user">User</Translate></th>
              <th><Translate contentKey="ykApp.payment.booking">Booking</Translate></th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredPayments.map(payment => (
              <tr key={payment.id} data-cy="paymentTable">
                <td>
                  <Link to={`/payment/${payment.id}`}>{payment.id}</Link>
                </td>
                <td>{payment.amount}</td>
                <td>{payment.paymentDate}</td>
                <td>{payment.status}</td>
                <td>{payment.user ? payment.user.login : ''}</td>
                <td>{payment.booking ? <Link to={`/booking/${payment.booking.id}`}>{payment.booking.id}</Link> : ''}</td>
                <td>
                  <Input
                    type="select"
                    name="statusUpdate"
                    id="statusUpdate"
                    value={payment.status}
                    onChange={e => handleStatusChange(payment.id, e.target.value)}
                  >
                    <option value="Pending">Pending</option>
                    <option value="Completed">Completed</option>
                    <option value="Failed">Failed</option>
                  </Input>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
};

export default AdminPaymentManagement;
