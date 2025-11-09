import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, updateVoucherForTier } from 'app/entities/tier-voucher/tier-voucher.reducer';
import { Table, Input } from 'reactstrap';
import { ClientTier } from 'app/shared/model/enumerations/client-tier.model';
import { Translate } from 'react-jhipster';

const VOUCHER_OPTIONS = [
  { value: 'RM10', label: 'RM10 off' },
  { value: 'RM15', label: 'RM15 off' },
  { value: 'RM20', label: 'RM20 off' },
  { value: 'RM25', label: 'RM25 off' },
  { value: '1_HOUR_FREE', label: '1 hour free of all sports' },
  { value: '2_HOUR_FREE', label: '2 hour free of all sports' },
];

const TIER_ORDER = [ClientTier.LEAD, ClientTier.IRON, ClientTier.GOLD, ClientTier.PLATINUM];

const ManageVoucher = () => {
  const dispatch = useAppDispatch();
  const tierVouchers = useAppSelector(state => state.tierVoucher.entities);
  const loading = useAppSelector(state => state.tierVoucher.loading);
  const updateSuccess = useAppSelector(state => state.tierVoucher.updateSuccess);

  useEffect(() => {
    dispatch(getEntities({}));
  }, [dispatch]);

  useEffect(() => {
    if (updateSuccess) {
      dispatch(getEntities({}));
    }
  }, [updateSuccess, dispatch]);

  const handleVoucherChange = (tier: ClientTier, voucherType: string) => {
    if (voucherType) {
      dispatch(updateVoucherForTier({ tier, voucherType }));
    }
  };

  const getVoucherForTier = (tier: ClientTier): string => {
    const tierVoucher = tierVouchers.find(tv => tv.tier === tier);
    return tierVoucher?.voucherType || '';
  };

  const getTierLabel = (tier: ClientTier): string => {
    switch (tier) {
      case ClientTier.LEAD:
        return 'Lead';
      case ClientTier.IRON:
        return 'Iron';
      case ClientTier.GOLD:
        return 'Gold';
      case ClientTier.PLATINUM:
        return 'Platinum';
      default:
        return tier;
    }
  };

  return (
    <div className="p-4">
      <h2>Manage Voucher</h2>

      {loading && <p>Loading tier vouchers...</p>}

      {!loading && (
        <Table responsive striped className="mt-4">
          <thead>
            <tr>
              <th>Client Tier</th>
              <th>Assigned Voucher</th>
            </tr>
          </thead>
          <tbody>
            {TIER_ORDER.map(tier => (
              <tr key={tier}>
                <td>
                  <strong>{getTierLabel(tier)}</strong>
                </td>
                <td>
                  <Input
                    type="select"
                    name={`voucher-${tier}`}
                    value={getVoucherForTier(tier)}
                    onChange={e => handleVoucherChange(tier, e.target.value)}
                    disabled={loading}
                    className="w-75"
                  >
                    <option value="">Select a voucher...</option>
                    {VOUCHER_OPTIONS.map(option => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </Input>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <div className="mt-4">
        <h4>Voucher Options:</h4>
        <ul>
          <li>RM10 - Recommended for LEAD tier</li>
          <li>RM15 - Recommended for IRON tier</li>
          <li>RM20 - Recommended for GOLD tier</li>
          <li>RM25 - Recommended for PLATINUM tier</li>
          <li>1 hour free - Recommended for GOLD tier</li>
          <li>2 hour free - Recommended for PLATINUM tier</li>
        </ul>
        <p className="text-muted">
          Note: Admin can assign any voucher to any tier. The recommendations are just suggestions based on tier levels.
        </p>
      </div>
    </div>
  );
};

export default ManageVoucher;
