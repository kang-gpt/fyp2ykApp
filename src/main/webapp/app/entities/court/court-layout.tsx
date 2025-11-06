import React from 'react';
import { Button } from 'reactstrap';

const CourtLayout = ({ court, onBack }) => {
  return (
    <div>
      <h2>Court Layout</h2>
      {court && (
        <div>
          <h3>{court.name}</h3>
          {/* Add more court layout details here */}
        </div>
      )}
      <Button onClick={onBack}>Back to Court List</Button>
    </div>
  );
};

export default CourtLayout;
