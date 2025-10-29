import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const BASKETBALL_PRICE = 75;

const Basketball = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate('/court?sport=basketball');
  }, [navigate]);

  return null;
};

export default Basketball;
