import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './client-tier.reducer';

export const ClientTierDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const clientTierEntity = useAppSelector(state => state.clientTier.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clientTierDetailsHeading">
          <Translate contentKey="ykApp.clientTier.detail.title">ClientTier</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{clientTierEntity.id}</dd>
          <dt>
            <span id="tierName">
              <Translate contentKey="ykApp.clientTier.tierName">Tier Name</Translate>
            </span>
          </dt>
          <dd>{clientTierEntity.tierName}</dd>
          <dt>
            <span id="discountPercentage">
              <Translate contentKey="ykApp.clientTier.discountPercentage">Discount Percentage</Translate>
            </span>
          </dt>
          <dd>{clientTierEntity.discountPercentage}</dd>
        </dl>
        <Button tag={Link} to="/client-tier" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/client-tier/${clientTierEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClientTierDetail;
