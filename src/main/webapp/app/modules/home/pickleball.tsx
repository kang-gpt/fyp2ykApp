import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const PICKLEBALL_PRICE = 50;

const Pickleball = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate('/court?sport=pickleball');
  }, [navigate]);

  return null;
};

export default Pickleball;
