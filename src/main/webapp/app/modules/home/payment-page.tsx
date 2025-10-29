import React from 'react';
import { useLocation } from 'react-router-dom';

const PaymentPage = () => {
  const location = useLocation();
  const { sport, hours, totalPrice, courtId, slotsToBook } = location.state || {};

  return (
    <div className="container my-5">
      <div className="card shadow p-4">
        <h2 className="card-title text-center mb-4">Payment Details</h2>

        {sport && hours && totalPrice ? (
          <div className="card-body">
            <h3 className="card-subtitle mb-3 text-primary">Booking Summary</h3>
            <div className="row mb-3">
              <div className="col-sm-6">
                <p>
                  <strong>Sport:</strong>
                </p>
              </div>
              <div className="col-sm-6">
                <p>{sport}</p>
              </div>

              <div className="col-sm-6">
                <p>
                  <strong>Court ID:</strong>
                </p>
              </div>
              <div className="col-sm-6">
                <p>{courtId}</p>
              </div>

              <div className="col-sm-6">
                <p>
                  <strong>Total Hours:</strong>
                </p>
              </div>
              <div className="col-sm-6">
                <p>{hours}</p>
              </div>

              <div className="col-sm-6">
                <p>
                  <strong>Court Price Per Hour:</strong>
                </p>
              </div>
              <div className="col-sm-6">
                <p>RM {totalPrice / hours}</p>
              </div>

              <div className="col-sm-6">
                <p>
                  <strong>Selected Slots:</strong>
                </p>
              </div>
              <div className="col-sm-6">
                <p>{slotsToBook.join(', ')}</p>
              </div>
            </div>

            <hr />

            <h4 className="text-center text-success display-6 mb-4">Total Price: **RM {totalPrice}**</h4>

            <div className="text-center mt-4 p-3 border rounded bg-light">
              <h4>Scan QR to Pay (Mock Payment)</h4>
              <img
                src="content/images/qrcode.jpg"
                alt="QR Code for Payment"
                className="img-fluid my-3"
                style={{ maxWidth: '250px', border: '1px solid #ddd' }}
              />
              <p className="text-muted small">
                *This is a mock payment screen. In a real application, you&apos;d integrate with a payment gateway.
              </p>
            </div>
          </div>
        ) : (
          <div className="alert alert-warning text-center" role="alert">
            <p className="mb-0">⚠️ No booking information available. Please return to the Time Slot selection page.</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentPage;
