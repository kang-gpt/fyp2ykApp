import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './court.reducer';

export const CourtDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const courtEntity = useAppSelector(state => state.court.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="courtDetailsHeading">
          <Translate contentKey="ykApp.court.detail.title">Court</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{courtEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="ykApp.court.name">Name</Translate>
            </span>
          </dt>
          <dd>{courtEntity.name}</dd>
          <dt>
            <Translate contentKey="ykApp.court.sport">Sport</Translate>
          </dt>
          <dd>{courtEntity.sport ? courtEntity.sport.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/courts" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/courts/${courtEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CourtDetail;
