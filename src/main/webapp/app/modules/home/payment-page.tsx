import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

const PaymentPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { sport, hours, totalPrice, courtId, slotsToBook } = location.state || {};
  const [showMessage, setShowMessage] = useState(false);
  const [copied, setCopied] = useState(false);

  const handleBack = () => {
    navigate(-1);
  };

  const handleDone = () => {
    setShowMessage(true);
  };

  const handleHome = () => {
    navigate('/');
  };

  const copyCourtDetails = () => {
    const details = `Sport: ${sport}\nCourt ID: ${courtId}\nSelected Slots: ${slotsToBook.join(', ')}\nTotal Hours: ${hours}\nTotal Price: RM ${totalPrice}`;
    navigator.clipboard.writeText(details);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

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

            <h4 className="text-center text-success display-6 mb-4">
              Total Price: <strong>RM {totalPrice}</strong>
            </h4>

            {/* Copy Court Details Section */}
            <div className="text-center my-4 p-3 border rounded bg-light">
              <h5 className="mb-2">Court Details for Bank Transfer</h5>
              <p className="mb-2 text-muted small">Copy and paste this as your payment reference during transfer:</p>
              <div className="d-flex justify-content-center align-items-center mb-2">
                <input
                  type="text"
                  readOnly
                  value={`Sport: ${sport} | Court: ${courtId} | Slots: ${slotsToBook.join(', ')} | RM ${totalPrice}`}
                  className="form-control w-75 text-center"
                />
                <button onClick={copyCourtDetails} className="btn btn-outline-primary ms-2">
                  {copied ? 'Copied!' : 'Copy'}
                </button>
              </div>
              {copied && <small className="text-success">✅ Court details copied to clipboard!</small>}
            </div>

            {/* QR Code Section */}
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

            {/* Buttons Section */}
            <div className="text-center mt-4">
              <button onClick={handleBack} className="btn btn-secondary mx-2 px-4">
                Back
              </button>
              <button onClick={handleHome} className="btn btn-primary mx-2 px-4">
                Home
              </button>
              <button onClick={handleDone} className="btn btn-success mx-2 px-4">
                Done
              </button>
            </div>

            {/* Success Message */}
            {showMessage && (
              <div className="alert alert-success mt-4 text-center" role="alert">
                ✅ Kindly check your email for booking confirmation.
              </div>
            )}
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
