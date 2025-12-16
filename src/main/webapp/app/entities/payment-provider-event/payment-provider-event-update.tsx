import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';
import { createEntity, getEntity, reset, updateEntity } from './payment-provider-event.reducer';

export const PaymentProviderEventUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.payment.entities);
  const paymentProviderEventEntity = useAppSelector(state => state.paymentProviderEvent.entity);
  const loading = useAppSelector(state => state.paymentProviderEvent.loading);
  const updating = useAppSelector(state => state.paymentProviderEvent.updating);
  const updateSuccess = useAppSelector(state => state.paymentProviderEvent.updateSuccess);
  const genericStatusValues = Object.keys(GenericStatus);

  const handleClose = () => {
    navigate(`/payment-provider-event${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPayments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.receivedAt = convertDateTimeToServer(values.receivedAt);
    values.processedAt = convertDateTimeToServer(values.processedAt);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...paymentProviderEventEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          receivedAt: displayDefaultDateTime(),
          processedAt: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          processingStatus: 'PENDING',
          ...paymentProviderEventEntity,
          receivedAt: convertDateTimeFromServer(paymentProviderEventEntity.receivedAt),
          processedAt: convertDateTimeFromServer(paymentProviderEventEntity.processedAt),
          deletedDate: convertDateTimeFromServer(paymentProviderEventEntity.deletedDate),
          payment: paymentProviderEventEntity?.payment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.paymentProviderEvent.home.createOrEditLabel" data-cy="PaymentProviderEventCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.paymentProviderEvent.home.createOrEditLabel">
              Create or edit a PaymentProviderEvent
            </Translate>
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
                  id="payment-provider-event-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.providerName')}
                id="payment-provider-event-providerName"
                name="providerName"
                data-cy="providerName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.eventType')}
                id="payment-provider-event-eventType"
                name="eventType"
                data-cy="eventType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 80, message: translate('entity.validation.maxlength', { max: 80 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.eventId')}
                id="payment-provider-event-eventId"
                name="eventId"
                data-cy="eventId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 120, message: translate('entity.validation.maxlength', { max: 120 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.payloadJson')}
                id="payment-provider-event-payloadJson"
                name="payloadJson"
                data-cy="payloadJson"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.receivedAt')}
                id="payment-provider-event-receivedAt"
                name="receivedAt"
                data-cy="receivedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.processedAt')}
                id="payment-provider-event-processedAt"
                name="processedAt"
                data-cy="processedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.processingStatus')}
                id="payment-provider-event-processingStatus"
                name="processingStatus"
                data-cy="processingStatus"
                type="select"
              >
                {genericStatusValues.map(genericStatus => (
                  <option value={genericStatus} key={genericStatus}>
                    {translate(`monsterdamChicksApp.GenericStatus.${genericStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.createdBy')}
                id="payment-provider-event-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.lastModifiedBy')}
                id="payment-provider-event-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.paymentProviderEvent.deletedDate')}
                id="payment-provider-event-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="payment-provider-event-payment"
                name="payment"
                data-cy="payment"
                label={translate('monsterdamChicksApp.paymentProviderEvent.payment')}
                type="select"
              >
                <option value="" key="0" />
                {payments
                  ? payments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment-provider-event" replace color="info">
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

export default PaymentProviderEventUpdate;
