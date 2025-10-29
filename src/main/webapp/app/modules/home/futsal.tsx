import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const FUTSAL_PRICE = 80;

const Futsal = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate('/court?sport=futsal');
  }, [navigate]);

  return null;
};

export default Futsal;
