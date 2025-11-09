import React, { useState, useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCourts, createEntity as createCourt, deleteEntity as deleteCourt } from 'app/entities/court/court.reducer';
import { getEntities as getSports } from 'app/entities/sport/sport.reducer';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Form, FormGroup, Label, Input, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';

const ManageCourtAvailability = () => {
  const dispatch = useAppDispatch();
  const courts = useAppSelector(state => state.court.entities);
  const sports = useAppSelector(state => state.sport.entities);
  const loading = useAppSelector(state => state.court.loading);
  const updateSuccess = useAppSelector(state => state.court.updateSuccess);

  const [modalOpen, setModalOpen] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    sportId: '',
  });

  useEffect(() => {
    dispatch(getCourts({}));
    dispatch(getSports({}));
  }, [dispatch]);

  useEffect(() => {
    if (updateSuccess) {
      setModalOpen(false);
      setFormData({ name: '', sportId: '' });
      dispatch(getCourts({}));
    }
  }, [updateSuccess, dispatch]);

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    if (!modalOpen) {
      setFormData({ name: '', sportId: '' });
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const selectedSport = sports.find(sport => sport.id?.toString() === formData.sportId);

    if (formData.name && selectedSport) {
      const newCourt = {
        name: formData.name,
        sport: selectedSport,
      };
      dispatch(createCourt(newCourt));
    }
  };

  const handleDelete = (courtId: number) => {
    if (window.confirm('Are you sure you want to delete this court?')) {
      dispatch(deleteCourt(courtId));
    }
  };

  return (
    <div className="p-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>
          <Translate contentKey="ykApp.court.home.title">Manage Court Availability</Translate>
        </h2>
        <Button
          color="success"
          size="lg"
          onClick={toggleModal}
          className="rounded-circle"
          style={{ width: '60px', height: '60px', fontSize: '32px' }}
        >
          +
        </Button>
      </div>

      {loading && <p>Loading courts...</p>}

      {!loading && courts.length === 0 && (
        <div className="alert alert-warning">
          <Translate contentKey="ykApp.court.home.notFound">No Courts found</Translate>
        </div>
      )}

      {!loading && courts.length > 0 && (
        <Table responsive striped>
          <thead>
            <tr>
              <th>Court ID</th>
              <th>Court Name</th>
              <th>Sport</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {courts.map(court => (
              <tr key={court.id}>
                <td>{court.id}</td>
                <td>{court.name}</td>
                <td>{court.sport?.name || 'N/A'}</td>
                <td>
                  <Button color="danger" size="sm" onClick={() => handleDelete(court.id!)}>
                    Delete
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}

      <Modal isOpen={modalOpen} toggle={toggleModal}>
        <ModalHeader toggle={toggleModal}>Add New Court</ModalHeader>
        <Form onSubmit={handleSubmit}>
          <ModalBody>
            <FormGroup>
              <Label for="sportId">Sport</Label>
              <Input type="select" name="sportId" id="sportId" value={formData.sportId} onChange={handleInputChange} required>
                <option value="">Select a sport...</option>
                {sports.map(sport => (
                  <option key={sport.id} value={sport.id}>
                    {sport.name}
                  </option>
                ))}
              </Input>
            </FormGroup>

            <FormGroup>
              <Label for="name">Court Name</Label>
              <Input
                type="text"
                name="name"
                id="name"
                placeholder="Enter court name (e.g., Court A, Court 1)"
                value={formData.name}
                onChange={handleInputChange}
                required
              />
            </FormGroup>
          </ModalBody>
          <ModalFooter>
            <Button color="secondary" onClick={toggleModal}>
              Cancel
            </Button>
            <Button color="primary" type="submit">
              Add Court
            </Button>
          </ModalFooter>
        </Form>
      </Modal>
    </div>
  );
};

export default ManageCourtAvailability;
