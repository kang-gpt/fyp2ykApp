import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';
import axios from 'axios';
import { IUser } from 'app/shared/model/user.model';
import { ITierVoucher } from 'app/shared/model/tier-voucher.model';

const PaymentPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { sport, hours, totalPrice, courtId, slotsToBook } = location.state || {};
  const [showMessage, setShowMessage] = useState(false);
  const [copied, setCopied] = useState(false);
  const [availableVoucher, setAvailableVoucher] = useState<ITierVoucher | null>(null);
  const [selectedVoucher, setSelectedVoucher] = useState<string | null>(null);
  const [finalPrice, setFinalPrice] = useState(totalPrice);
  const [loadingVoucher, setLoadingVoucher] = useState(false);

  const currentUser = useAppSelector(state => state.authentication.account as IUser);

  // Fetch available voucher based on user's tier
  useEffect(() => {
    const fetchVoucher = async () => {
      if (currentUser && currentUser.tier) {
        setLoadingVoucher(true);
        try {
          const response = await axios.get<ITierVoucher>(`api/tier-vouchers/tier/${currentUser.tier}`);
          setAvailableVoucher(response.data);
        } catch (error) {
          console.error('Error fetching voucher:', error);
          setAvailableVoucher(null);
        } finally {
          setLoadingVoucher(false);
        }
      }
    };

    fetchVoucher();
  }, [currentUser]);

  // Calculate voucher discount
  const calculateDiscount = (voucherType: string): number => {
    if (voucherType.includes('RM')) {
      const amount = parseFloat(voucherType.replace('RM', ''));
      return amount;
    } else if (voucherType.includes('HOUR_FREE')) {
      const hours = parseInt(voucherType.split('_')[0]);
      const pricePerHour = totalPrice / hours;
      return pricePerHour * hours;
    }
    return 0;
  };

  // Handle voucher selection
  const handleVoucherChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const isChecked = event.target.checked;
    if (isChecked && availableVoucher) {
      setSelectedVoucher(availableVoucher.voucherType);
      const discount = calculateDiscount(availableVoucher.voucherType);
      setFinalPrice(Math.max(0, totalPrice - discount));
    } else {
      setSelectedVoucher(null);
      setFinalPrice(totalPrice);
    }
  };

  const handleBack = () => {
    navigate(-1);
  };

  const handleDone = async () => {
    try {
      // Create payment records for each booked slot
      const paymentPromises = slotsToBook.map(async slot => {
        const [dateString, time] = slot.split(' ');
        const bookingDate = new Date(`${dateString}T${time}`);

        // Find the booking for this slot
        const bookingsResponse = await axios.get(`/api/bookings`, {
          params: {
            'user.id.equals': currentUser.id,
            'bookingDate.equals': bookingDate.toISOString(),
            'status.equals': 'PENDING',
          },
        });

        if (bookingsResponse.data && bookingsResponse.data.length > 0) {
          const booking = bookingsResponse.data[0];

          // Create payment for this booking
          const payment = {
            amount: finalPrice / slotsToBook.length, // Split total amount across all slots
            paymentDate: new Date().toISOString(),
            status: 'PENDING',
            booking: { id: booking.id },
            user: { id: currentUser.id },
          };

          await axios.post('/api/payments', payment);
        }
      });

      await Promise.all(paymentPromises);
      setShowMessage(true);
    } catch (error) {
      console.error('Error creating payment:', error);
      alert('Failed to process payment. Please try again.');
    }
  };

  const handleHome = () => {
    navigate('/');
  };

  const copyCourtDetails = () => {
    const voucherText = selectedVoucher
      ? `\nVoucher Applied: ${selectedVoucher}\nDiscount: RM ${(totalPrice - finalPrice).toFixed(2)}`
      : '';
    const details = `Sport: ${sport}\nCourt ID: ${courtId}\nSelected Slots: ${slotsToBook.join(', ')}\nTotal Hours: ${hours}\nOriginal Price: RM ${totalPrice}${voucherText}\nFinal Price: RM ${finalPrice}`;
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

            {/* Voucher Selection Section */}
            {loadingVoucher ? (
              <div className="text-center my-3">
                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                Loading available vouchers...
              </div>
            ) : availableVoucher ? (
              <div className="my-4 p-3 border rounded bg-light">
                <h5 className="mb-3">Available Voucher</h5>
                <div className="form-check">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    id="voucherCheckbox"
                    checked={selectedVoucher !== null}
                    onChange={handleVoucherChange}
                  />
                  <label className="form-check-label" htmlFor="voucherCheckbox">
                    <strong>{availableVoucher.voucherType}</strong> - {availableVoucher.tier} Tier Voucher
                    {selectedVoucher && (
                      <span className="text-success ms-2">✅ Applied (Save RM {(totalPrice - finalPrice).toFixed(2)})</span>
                    )}
                  </label>
                </div>
              </div>
            ) : currentUser && currentUser.tier ? (
              <div className="my-4 p-3 border rounded bg-light">
                <p className="text-muted mb-0">No vouchers available for your tier ({currentUser.tier}).</p>
              </div>
            ) : null}

            <hr />

            {/* Price Display with Voucher Breakdown */}
            {selectedVoucher ? (
              <div className="mb-4">
                <div className="d-flex justify-content-between mb-2">
                  <span>Original Price:</span>
                  <span>RM {totalPrice}</span>
                </div>
                <div className="d-flex justify-content-between mb-2 text-success">
                  <span>Voucher Discount ({selectedVoucher}):</span>
                  <span>- RM {(totalPrice - finalPrice).toFixed(2)}</span>
                </div>
                <hr />
                <h4 className="text-center text-success display-6 mb-4">
                  Final Price: <strong>RM {finalPrice.toFixed(2)}</strong>
                </h4>
              </div>
            ) : (
              <h4 className="text-center text-success display-6 mb-4">
                Total Price: <strong>RM {totalPrice}</strong>
              </h4>
            )}

            {/* Copy Court Details Section */}
            <div className="text-center my-4 p-3 border rounded bg-light">
              <h5 className="mb-2">Court Details for Bank Transfer</h5>
              <p className="mb-2 text-muted small">Copy and paste this as your payment reference during transfer:</p>
              <div className="d-flex justify-content-center align-items-center mb-2">
                <input
                  type="text"
                  readOnly
                  value={`Sport: ${sport} | Court: ${courtId} | Slots: ${slotsToBook.join(', ')} | ${selectedVoucher ? `Voucher: ${selectedVoucher} | ` : ''}RM ${finalPrice.toFixed(2)}`}
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
