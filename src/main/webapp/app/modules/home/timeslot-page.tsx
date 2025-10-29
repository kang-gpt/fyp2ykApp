import React, { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import TimeSlot from './TimeSlot';
import { Translate } from 'react-jhipster';
import { Table, Button } from 'reactstrap';

interface ICourt {
  id: number;
  name: string;
  sport: { id: number; name: string };
}

const TimeSlotPage = () => {
  const { courtId: pathCourtId, sportId: pathSportId } = useParams<{ courtId: string; sportId: string }>();
  const location = useLocation();
  const navigate = useNavigate();
  const queryParams = new URLSearchParams(location.search);
  const selectedSportName = queryParams.get('sport');

  const [courts, setCourts] = useState<ICourt[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const API_URL = '/api/courts';

  useEffect(() => {
    if (selectedSportName) {
      setLoading(true);
      setError(null);
      axios.get<ICourt[]>(`${API_URL}?sportName=${selectedSportName}`)
        .then(response => {
          setCourts(response.data);
        })
        .catch(err => {
          setError('Failed to fetch courts.');
          console.error(err);
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [selectedSportName]);

  const handleCourtClick = (courtId: number, sportId: number) => {
    navigate(`/${sportId}/court/${courtId}`);
  };

  if (pathCourtId && pathSportId) {
    return <TimeSlot courtId={pathCourtId} sportId={pathSportId} />;
  }

  return (
    <div>
      <h2 id="court-heading" data-cy="CourtHeading">
        {selectedSportName ? (
          <>
            <Translate contentKey="ykApp.court.home.courtsFor">Courts for</Translate> <strong>{selectedSportName}</strong>
          </>
        ) : (
          <Translate contentKey="ykApp.court.home.title">Courts</Translate>
        )}
      </h2>

      {loading && <p>Loading courts...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && courts.length === 0 && selectedSportName && (
        <div className="alert alert-warning">
          <Translate contentKey="ykApp.court.home.noCourtsFoundForSport">No courts found for this sport.</Translate>
        </div>
      )}

      {!loading && !error && courts.length > 0 && (
        <Table responsive striped>
          <thead>
            <tr>
              <th>ID</th>
              <th><Translate contentKey="ykApp.court.name">Name</Translate></th>
              <th><Translate contentKey="ykApp.court.sport">Sport</Translate></th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {courts.map(court => (
              <tr key={court.id}>
                <td>{court.id}</td>
                <td>{court.name}</td>
                <td>{court.sport ? court.sport.name : 'N/A'}</td>
                <td>
                  <Button color="primary" size="sm" onClick={() => handleCourtClick(court.id, court.sport.id)}>
                    <Translate contentKey="ykApp.court.home.viewTimeSlots">View Time Slots</Translate>
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
};

export default TimeSlotPage;
