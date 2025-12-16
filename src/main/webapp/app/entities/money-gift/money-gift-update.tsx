import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { createEntity, getEntity, reset, updateEntity } from './money-gift.reducer';

export const MoneyGiftUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.payment.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const moneyGiftEntity = useAppSelector(state => state.moneyGift.entity);
  const loading = useAppSelector(state => state.moneyGift.loading);
  const updating = useAppSelector(state => state.moneyGift.updating);
  const updateSuccess = useAppSelector(state => state.moneyGift.updateSuccess);

  const handleClose = () => {
    navigate(`/money-gift${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPayments({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);
    if (values.messageId !== undefined && typeof values.messageId !== 'number') {
      values.messageId = Number(values.messageId);
    }
    if (values.postId !== undefined && typeof values.postId !== 'number') {
      values.postId = Number(values.postId);
    }

    const entity = {
      ...moneyGiftEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
      viewer: userLites.find(it => it.id.toString() === values.viewer?.toString()),
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
          ...moneyGiftEntity,
          createdDate: convertDateTimeFromServer(moneyGiftEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(moneyGiftEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(moneyGiftEntity.deletedDate),
          payment: moneyGiftEntity?.payment?.id,
          viewer: moneyGiftEntity?.viewer?.id,
          creator: moneyGiftEntity?.creator?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.moneyGift.home.createOrEditLabel" data-cy="MoneyGiftCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.moneyGift.home.createOrEditLabel">Create or edit a MoneyGift</Translate>
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
                  id="money-gift-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.amount')}
                id="money-gift-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.currency')}
                id="money-gift-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.createdDate')}
                id="money-gift-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.lastModifiedDate')}
                id="money-gift-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.createdBy')}
                id="money-gift-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.lastModifiedBy')}
                id="money-gift-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.deletedDate')}
                id="money-gift-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.messageId')}
                id="money-gift-messageId"
                name="messageId"
                data-cy="messageId"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.moneyGift.postId')}
                id="money-gift-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <ValidatedField
                id="money-gift-payment"
                name="payment"
                data-cy="payment"
                label={translate('monsterdamChicksApp.moneyGift.payment')}
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
              <ValidatedField
                id="money-gift-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('monsterdamChicksApp.moneyGift.viewer')}
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
              <ValidatedField
                id="money-gift-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.moneyGift.creator')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/money-gift" replace color="info">
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

export default MoneyGiftUpdate;
