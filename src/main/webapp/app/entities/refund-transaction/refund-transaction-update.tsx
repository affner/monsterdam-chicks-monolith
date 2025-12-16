import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';
import { createEntity, getEntity, reset, updateEntity } from './refund-transaction.reducer';

export const RefundTransactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.payment.entities);
  const refundTransactionEntity = useAppSelector(state => state.refundTransaction.entity);
  const loading = useAppSelector(state => state.refundTransaction.loading);
  const updating = useAppSelector(state => state.refundTransaction.updating);
  const updateSuccess = useAppSelector(state => state.refundTransaction.updateSuccess);
  const genericStatusValues = Object.keys(GenericStatus);

  const handleClose = () => {
    navigate(`/refund-transaction${location.search}`);
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.processedAt = convertDateTimeToServer(values.processedAt);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...refundTransactionEntity,
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
          createdDate: displayDefaultDateTime(),
          processedAt: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          status: 'PENDING',
          ...refundTransactionEntity,
          createdDate: convertDateTimeFromServer(refundTransactionEntity.createdDate),
          processedAt: convertDateTimeFromServer(refundTransactionEntity.processedAt),
          deletedDate: convertDateTimeFromServer(refundTransactionEntity.deletedDate),
          payment: refundTransactionEntity?.payment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.refundTransaction.home.createOrEditLabel" data-cy="RefundTransactionCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.refundTransaction.home.createOrEditLabel">
              Create or edit a RefundTransaction
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
                  id="refund-transaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.amount')}
                id="refund-transaction-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.currency')}
                id="refund-transaction-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.reason')}
                id="refund-transaction-reason"
                name="reason"
                data-cy="reason"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.paymentReference')}
                id="refund-transaction-paymentReference"
                name="paymentReference"
                data-cy="paymentReference"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.providerChargeId')}
                id="refund-transaction-providerChargeId"
                name="providerChargeId"
                data-cy="providerChargeId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.status')}
                id="refund-transaction-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {genericStatusValues.map(genericStatus => (
                  <option value={genericStatus} key={genericStatus}>
                    {translate(`monsterdamChicksApp.GenericStatus.${genericStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.createdDate')}
                id="refund-transaction-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.processedAt')}
                id="refund-transaction-processedAt"
                name="processedAt"
                data-cy="processedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.createdBy')}
                id="refund-transaction-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.lastModifiedBy')}
                id="refund-transaction-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.refundTransaction.deletedDate')}
                id="refund-transaction-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="refund-transaction-payment"
                name="payment"
                data-cy="payment"
                label={translate('monsterdamChicksApp.refundTransaction.payment')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/refund-transaction" replace color="info">
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

export default RefundTransactionUpdate;
