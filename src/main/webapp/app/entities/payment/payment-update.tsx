import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPaymentMethods } from 'app/entities/payment-method/payment-method.reducer';
import { getEntities as getPaymentProviders } from 'app/entities/payment-provider/payment-provider.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';
import { createEntity, getEntity, reset, updateEntity } from './payment.reducer';

export const PaymentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const paymentMethods = useAppSelector(state => state.paymentMethod.entities);
  const paymentProviders = useAppSelector(state => state.paymentProvider.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const paymentEntity = useAppSelector(state => state.payment.entity);
  const loading = useAppSelector(state => state.payment.loading);
  const updating = useAppSelector(state => state.payment.updating);
  const updateSuccess = useAppSelector(state => state.payment.updateSuccess);
  const genericStatusValues = Object.keys(GenericStatus);

  const handleClose = () => {
    navigate(`/payment${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPaymentMethods({}));
    dispatch(getPaymentProviders({}));
    dispatch(getUserLites({}));
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
    values.paymentDate = convertDateTimeToServer(values.paymentDate);
    if (values.providerFeeAmount !== undefined && typeof values.providerFeeAmount !== 'number') {
      values.providerFeeAmount = Number(values.providerFeeAmount);
    }
    if (values.platformFeeAmount !== undefined && typeof values.platformFeeAmount !== 'number') {
      values.platformFeeAmount = Number(values.platformFeeAmount);
    }
    if (values.creatorNetAmount !== undefined && typeof values.creatorNetAmount !== 'number') {
      values.creatorNetAmount = Number(values.creatorNetAmount);
    }
    if (values.taxAmount !== undefined && typeof values.taxAmount !== 'number') {
      values.taxAmount = Number(values.taxAmount);
    }
    values.authorizedDate = convertDateTimeToServer(values.authorizedDate);
    values.capturedDate = convertDateTimeToServer(values.capturedDate);

    const entity = {
      ...paymentEntity,
      ...values,
      method: paymentMethods.find(it => it.id.toString() === values.method?.toString()),
      provider: paymentProviders.find(it => it.id.toString() === values.provider?.toString()),
      viewer: userLites.find(it => it.id.toString() === values.viewer?.toString()),
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
          paymentDate: displayDefaultDateTime(),
          authorizedDate: displayDefaultDateTime(),
          capturedDate: displayDefaultDateTime(),
        }
      : {
          paymentStatus: 'PENDING',
          ...paymentEntity,
          paymentDate: convertDateTimeFromServer(paymentEntity.paymentDate),
          authorizedDate: convertDateTimeFromServer(paymentEntity.authorizedDate),
          capturedDate: convertDateTimeFromServer(paymentEntity.capturedDate),
          method: paymentEntity?.method?.id,
          provider: paymentEntity?.provider?.id,
          viewer: paymentEntity?.viewer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.payment.home.createOrEditLabel" data-cy="PaymentCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.payment.home.createOrEditLabel">Create or edit a Payment</Translate>
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
                  id="payment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.amount')}
                id="payment-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.currency')}
                id="payment-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.paymentDate')}
                id="payment-paymentDate"
                name="paymentDate"
                data-cy="paymentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.paymentStatus')}
                id="payment-paymentStatus"
                name="paymentStatus"
                data-cy="paymentStatus"
                type="select"
              >
                {genericStatusValues.map(genericStatus => (
                  <option value={genericStatus} key={genericStatus}>
                    {translate(`monsterdamChicksApp.GenericStatus.${genericStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.paymentReference')}
                id="payment-paymentReference"
                name="paymentReference"
                data-cy="paymentReference"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.cloudTransactionId')}
                id="payment-cloudTransactionId"
                name="cloudTransactionId"
                data-cy="cloudTransactionId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.providerPaymentIntentId')}
                id="payment-providerPaymentIntentId"
                name="providerPaymentIntentId"
                data-cy="providerPaymentIntentId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.providerChargeId')}
                id="payment-providerChargeId"
                name="providerChargeId"
                data-cy="providerChargeId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.providerCustomerId')}
                id="payment-providerCustomerId"
                name="providerCustomerId"
                data-cy="providerCustomerId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.providerPaymentMethodId')}
                id="payment-providerPaymentMethodId"
                name="providerPaymentMethodId"
                data-cy="providerPaymentMethodId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.providerEventLastId')}
                id="payment-providerEventLastId"
                name="providerEventLastId"
                data-cy="providerEventLastId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.countryCode')}
                id="payment-countryCode"
                name="countryCode"
                data-cy="countryCode"
                type="text"
                validate={{
                  maxLength: { value: 2, message: translate('entity.validation.maxlength', { max: 2 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.providerFeeAmount')}
                id="payment-providerFeeAmount"
                name="providerFeeAmount"
                data-cy="providerFeeAmount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.platformFeeAmount')}
                id="payment-platformFeeAmount"
                name="platformFeeAmount"
                data-cy="platformFeeAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.creatorNetAmount')}
                id="payment-creatorNetAmount"
                name="creatorNetAmount"
                data-cy="creatorNetAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.taxAmount')}
                id="payment-taxAmount"
                name="taxAmount"
                data-cy="taxAmount"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.authorizedDate')}
                id="payment-authorizedDate"
                name="authorizedDate"
                data-cy="authorizedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.payment.capturedDate')}
                id="payment-capturedDate"
                name="capturedDate"
                data-cy="capturedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="payment-method"
                name="method"
                data-cy="method"
                label={translate('monsterdamChicksApp.payment.method')}
                type="select"
              >
                <option value="" key="0" />
                {paymentMethods
                  ? paymentMethods.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="payment-provider"
                name="provider"
                data-cy="provider"
                label={translate('monsterdamChicksApp.payment.provider')}
                type="select"
              >
                <option value="" key="0" />
                {paymentProviders
                  ? paymentProviders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="payment-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('monsterdamChicksApp.payment.viewer')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userLites
                  ? userLites.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/payment" replace color="info">
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

export default PaymentUpdate;
