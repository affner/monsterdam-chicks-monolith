import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { ViewerWalletTransactionType } from 'app/shared/model/enumerations/viewer-wallet-transaction-type.model';
import { createEntity, getEntity, reset, updateEntity } from './viewer-wallet.reducer';

export const ViewerWalletUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const payments = useAppSelector(state => state.payment.entities);
  const userLites = useAppSelector(state => state.userLite.entities);
  const viewerWalletEntity = useAppSelector(state => state.viewerWallet.entity);
  const loading = useAppSelector(state => state.viewerWallet.loading);
  const updating = useAppSelector(state => state.viewerWallet.updating);
  const updateSuccess = useAppSelector(state => state.viewerWallet.updateSuccess);
  const viewerWalletTransactionTypeValues = Object.keys(ViewerWalletTransactionType);

  const handleClose = () => {
    navigate(`/viewer-wallet${location.search}`);
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

    const entity = {
      ...viewerWalletEntity,
      ...values,
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          transactionType: 'TOP_UP',
          ...viewerWalletEntity,
          createdDate: convertDateTimeFromServer(viewerWalletEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(viewerWalletEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(viewerWalletEntity.deletedDate),
          payment: viewerWalletEntity?.payment?.id,
          viewer: viewerWalletEntity?.viewer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.viewerWallet.home.createOrEditLabel" data-cy="ViewerWalletCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.viewerWallet.home.createOrEditLabel">Create or edit a ViewerWallet</Translate>
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
                  id="viewer-wallet-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.amount')}
                id="viewer-wallet-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.currency')}
                id="viewer-wallet-currency"
                name="currency"
                data-cy="currency"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 3, message: translate('entity.validation.maxlength', { max: 3 }) },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.transactionType')}
                id="viewer-wallet-transactionType"
                name="transactionType"
                data-cy="transactionType"
                type="select"
              >
                {viewerWalletTransactionTypeValues.map(viewerWalletTransactionType => (
                  <option value={viewerWalletTransactionType} key={viewerWalletTransactionType}>
                    {translate(`monsterdamChicksApp.ViewerWalletTransactionType.${viewerWalletTransactionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.createdDate')}
                id="viewer-wallet-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.lastModifiedDate')}
                id="viewer-wallet-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.createdBy')}
                id="viewer-wallet-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.lastModifiedBy')}
                id="viewer-wallet-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.viewerWallet.deletedDate')}
                id="viewer-wallet-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="viewer-wallet-payment"
                name="payment"
                data-cy="payment"
                label={translate('monsterdamChicksApp.viewerWallet.payment')}
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
                id="viewer-wallet-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('monsterdamChicksApp.viewerWallet.viewer')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/viewer-wallet" replace color="info">
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

export default ViewerWalletUpdate;
