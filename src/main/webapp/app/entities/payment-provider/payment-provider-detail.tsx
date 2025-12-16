import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-provider.reducer';

export const PaymentProviderDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentProviderEntity = useAppSelector(state => state.paymentProvider.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentProviderDetailsHeading">
          <Translate contentKey="monsterdamChicksApp.paymentProvider.detail.title">PaymentProvider</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.id}</dd>
          <dt>
            <span id="providerName">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.providerName">Provider Name</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.providerName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.description">Description</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.description}</dd>
          <dt>
            <span id="apiKeyText">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.apiKeyText">Api Key Text</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.apiKeyText}</dd>
          <dt>
            <span id="apiSecretText">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.apiSecretText">Api Secret Text</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.apiSecretText}</dd>
          <dt>
            <span id="endpointText">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.endpointText">Endpoint Text</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.endpointText}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEntity.createdDate ? (
              <TextFormat value={paymentProviderEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEntity.lastModifiedDate ? (
              <TextFormat value={paymentProviderEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.createdBy}</dd>
          <dt>
            <span id="lastModifiedBy">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.lastModifiedBy">Last Modified By</Translate>
            </span>
          </dt>
          <dd>{paymentProviderEntity.lastModifiedBy}</dd>
          <dt>
            <span id="deletedDate">
              <Translate contentKey="monsterdamChicksApp.paymentProvider.deletedDate">Deleted Date</Translate>
            </span>
          </dt>
          <dd>
            {paymentProviderEntity.deletedDate ? (
              <TextFormat value={paymentProviderEntity.deletedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/payment-provider" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-provider/${paymentProviderEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentProviderDetail;
