import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const BADMINTON_PRICE = 25;

const Badminton = () => {
  const navigate = useNavigate();

  useEffect(() => {
    navigate('/court?sport=badminton');
  }, [navigate]);

  return null;
};

export default Badminton;
