import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { MoneyEarningType } from 'app/shared/model/enumerations/money-earning-type.model';
import { createEntity, getEntity, reset, updateEntity } from './money-earning.reducer';

export const MoneyEarningUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const moneyEarningEntity = useAppSelector(state => state.moneyEarning.entity);
  const loading = useAppSelector(state => state.moneyEarning.loading);
  const updating = useAppSelector(state => state.moneyEarning.updating);
  const updateSuccess = useAppSelector(state => state.moneyEarning.updateSuccess);
  const moneyEarningTypeValues = Object.keys(MoneyEarningType);

  const handleClose = () => {
    navigate(`/money-earning${location.search}`);
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
    if (values.sourceId !== undefined && typeof values.sourceId !== 'number') {
      values.sourceId = Number(values.sourceId);
    }
    if (values.payoutId !== undefined && typeof values.payoutId !== 'number') {
      values.payoutId = Number(values.payoutId);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...moneyEarningEntity,
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          transactionType: 'TIP_EARNING',
          ...moneyEarningEntity,
          createdDate: convertDateTimeFromServer(moneyEarningEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(moneyEarningEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(moneyEarningEntity.deletedDate),
          creator: moneyEarningEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.moneyEarning.home.createOrEditLabel" data-cy="MoneyEarningCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.moneyEarning.home.createOrEditLabel">Create or edit a MoneyEarning</Translate>
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
                  id="money-earning-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.amount')}
                id="money-earning-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.currency')}
                id="money-earning-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.transactionType')}
                id="money-earning-transactionType"
                name="transactionType"
                data-cy="transactionType"
                type="select"
              >
                {moneyEarningTypeValues.map(moneyEarningType => (
                  <option value={moneyEarningType} key={moneyEarningType}>
                    {translate(`monsterdamChicksApp.MoneyEarningType.${moneyEarningType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.sourceType')}
                id="money-earning-sourceType"
                name="sourceType"
                data-cy="sourceType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 30, message: translate('entity.validation.maxlength', { max: 30 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.sourceId')}
                id="money-earning-sourceId"
                name="sourceId"
                data-cy="sourceId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.isPaid')}
                id="money-earning-isPaid"
                name="isPaid"
                data-cy="isPaid"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.payoutId')}
                id="money-earning-payoutId"
                name="payoutId"
                data-cy="payoutId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.createdDate')}
                id="money-earning-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.lastModifiedDate')}
                id="money-earning-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.createdBy')}
                id="money-earning-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.lastModifiedBy')}
                id="money-earning-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyEarning.deletedDate')}
                id="money-earning-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="money-earning-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.moneyEarning.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/money-earning" replace color="info">
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

export default MoneyEarningUpdate;
