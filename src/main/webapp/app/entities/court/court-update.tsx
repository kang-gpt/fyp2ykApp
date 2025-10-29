import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, Form, FormGroup, Label, Input } from 'reactstrap';
import { Translate, translate, isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISport } from 'app/shared/model/sport.model';
import { getEntities as getSports } from 'app/entities/sport/sport.reducer';
import { ICourt } from 'app/shared/model/court.model';
import { getEntity, updateEntity, createEntity, reset } from './court.reducer';

export const CourtUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams();
  const isNew = id === undefined;

  const sports = useAppSelector(state => state.sport.entities);
  const courtEntity = useAppSelector(state => state.court.entity);
  const loading = useAppSelector(state => state.court.loading);
  const updating = useAppSelector(state => state.court.updating);
  const updateSuccess = useAppSelector(state => state.court.updateSuccess);

  const handleClose = () => {
    navigate('/courts');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSports({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...courtEntity,
      ...values,
      sport: sports.find(it => it.id.toString() === values.sport.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () => (isNew ? {} : { ...courtEntity, sport: courtEntity?.sport?.id });

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ykApp.court.home.createOrEditLabel" data-cy="CourtCreateUpdateHeading">
            <Translate contentKey="ykApp.court.home.createOrEditLabel">Create or edit a Court</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="court-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('ykApp.court.name')} id="court-name" name="name" data-cy="name" type="text" />
              <ValidatedField id="court-sport" name="sport" data-cy="sport" label={translate('ykApp.court.sport')} type="select">
                <option value="" key="0" />
                {sports
                  ? sports.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/courts" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CourtUpdate;
