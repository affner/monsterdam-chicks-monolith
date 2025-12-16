import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { MoneyWithdrawStatus } from 'app/shared/model/enumerations/money-withdraw-status.model';
import { createEntity, getEntity, reset, updateEntity } from './money-withdraw.reducer';

export const MoneyWithdrawUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const moneyWithdrawEntity = useAppSelector(state => state.moneyWithdraw.entity);
  const loading = useAppSelector(state => state.moneyWithdraw.loading);
  const updating = useAppSelector(state => state.moneyWithdraw.updating);
  const updateSuccess = useAppSelector(state => state.moneyWithdraw.updateSuccess);
  const moneyWithdrawStatusValues = Object.keys(MoneyWithdrawStatus);

  const handleClose = () => {
    navigate(`/money-withdraw${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    values.processedAt = convertDateTimeToServer(values.processedAt);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...moneyWithdrawEntity,
      ...values,
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
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
          processedAt: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          withdrawStatus: 'WITHDRAW_PENDING',
          ...moneyWithdrawEntity,
          processedAt: convertDateTimeFromServer(moneyWithdrawEntity.processedAt),
          createdDate: convertDateTimeFromServer(moneyWithdrawEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(moneyWithdrawEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(moneyWithdrawEntity.deletedDate),
          creator: moneyWithdrawEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.moneyWithdraw.home.createOrEditLabel" data-cy="MoneyWithdrawCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.moneyWithdraw.home.createOrEditLabel">Create or edit a MoneyWithdraw</Translate>
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
                  id="money-withdraw-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.amount')}
                id="money-withdraw-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.currency')}
                id="money-withdraw-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.payoutProviderName')}
                id="money-withdraw-payoutProviderName"
                name="payoutProviderName"
                data-cy="payoutProviderName"
                type="text"
                validate={{
                  maxLength: { value: 50, message: translate('entity.validation.maxlength', { max: 50 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.payoutReferenceId')}
                id="money-withdraw-payoutReferenceId"
                name="payoutReferenceId"
                data-cy="payoutReferenceId"
                type="text"
                validate={{
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.processedAt')}
                id="money-withdraw-processedAt"
                name="processedAt"
                data-cy="processedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.createdDate')}
                id="money-withdraw-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.lastModifiedDate')}
                id="money-withdraw-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.createdBy')}
                id="money-withdraw-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.lastModifiedBy')}
                id="money-withdraw-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.deletedDate')}
                id="money-withdraw-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyWithdraw.withdrawStatus')}
                id="money-withdraw-withdrawStatus"
                name="withdrawStatus"
                data-cy="withdrawStatus"
                type="select"
              >
                {moneyWithdrawStatusValues.map(moneyWithdrawStatus => (
                  <option value={moneyWithdrawStatus} key={moneyWithdrawStatus}>
                    {translate(`monsterdamChicksApp.MoneyWithdrawStatus.${moneyWithdrawStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="money-withdraw-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.moneyWithdraw.creator')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/money-withdraw" replace color="info">
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

export default MoneyWithdrawUpdate;
