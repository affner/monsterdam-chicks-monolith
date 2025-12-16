import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUserLites } from 'app/entities/user-lite/user-lite.reducer';
import { getEntities as getPayments } from 'app/entities/payment/payment.reducer';
import { getEntities as getSubscriptionPlanOffers } from 'app/entities/subscription-plan-offer/subscription-plan-offer.reducer';
import { PurchasedSubscriptionStatus } from 'app/shared/model/enumerations/purchased-subscription-status.model';
import { createEntity, getEntity, reset, updateEntity } from './purchased-subscription.reducer';

export const PurchasedSubscriptionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userLites = useAppSelector(state => state.userLite.entities);
  const payments = useAppSelector(state => state.payment.entities);
  const subscriptionPlanOffers = useAppSelector(state => state.subscriptionPlanOffer.entities);
  const purchasedSubscriptionEntity = useAppSelector(state => state.purchasedSubscription.entity);
  const loading = useAppSelector(state => state.purchasedSubscription.loading);
  const updating = useAppSelector(state => state.purchasedSubscription.updating);
  const updateSuccess = useAppSelector(state => state.purchasedSubscription.updateSuccess);
  const purchasedSubscriptionStatusValues = Object.keys(PurchasedSubscriptionStatus);

  const handleClose = () => {
    navigate(`/purchased-subscription${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserLites({}));
    dispatch(getPayments({}));
    dispatch(getSubscriptionPlanOffers({}));
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);
    values.deletedDate = convertDateTimeToServer(values.deletedDate);

    const entity = {
      ...purchasedSubscriptionEntity,
      ...values,
      viewer: userLites.find(it => it.id.toString() === values.viewer?.toString()),
      creator: userLites.find(it => it.id.toString() === values.creator?.toString()),
      payment: payments.find(it => it.id.toString() === values.payment?.toString()),
      subscriptionPlanOffer: subscriptionPlanOffers.find(it => it.id.toString() === values.subscriptionPlanOffer?.toString()),
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
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
          deletedDate: displayDefaultDateTime(),
        }
      : {
          subscriptionStatus: 'PURCHASED',
          ...purchasedSubscriptionEntity,
          startDate: convertDateTimeFromServer(purchasedSubscriptionEntity.startDate),
          endDate: convertDateTimeFromServer(purchasedSubscriptionEntity.endDate),
          createdDate: convertDateTimeFromServer(purchasedSubscriptionEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(purchasedSubscriptionEntity.lastModifiedDate),
          deletedDate: convertDateTimeFromServer(purchasedSubscriptionEntity.deletedDate),
          viewer: purchasedSubscriptionEntity?.viewer?.id,
          creator: purchasedSubscriptionEntity?.creator?.id,
          payment: purchasedSubscriptionEntity?.payment?.id,
          subscriptionPlanOffer: purchasedSubscriptionEntity?.subscriptionPlanOffer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="monsterdamChicksApp.purchasedSubscription.home.createOrEditLabel" data-cy="PurchasedSubscriptionCreateUpdateHeading">
            <Translate contentKey="monsterdamChicksApp.purchasedSubscription.home.createOrEditLabel">
              Create or edit a PurchasedSubscription
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
                  id="purchased-subscription-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.startDate')}
                id="purchased-subscription-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.endDate')}
                id="purchased-subscription-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.subscriptionStatus')}
                id="purchased-subscription-subscriptionStatus"
                name="subscriptionStatus"
                data-cy="subscriptionStatus"
                type="select"
              >
                {purchasedSubscriptionStatusValues.map(purchasedSubscriptionStatus => (
                  <option value={purchasedSubscriptionStatus} key={purchasedSubscriptionStatus}>
                    {translate(`monsterdamChicksApp.PurchasedSubscriptionStatus.${purchasedSubscriptionStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.createdDate')}
                id="purchased-subscription-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.lastModifiedDate')}
                id="purchased-subscription-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.createdBy')}
                id="purchased-subscription-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.lastModifiedBy')}
                id="purchased-subscription-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('monsterdamChicksApp.purchasedSubscription.deletedDate')}
                id="purchased-subscription-deletedDate"
                name="deletedDate"
                data-cy="deletedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="purchased-subscription-viewer"
                name="viewer"
                data-cy="viewer"
                label={translate('monsterdamChicksApp.purchasedSubscription.viewer')}
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
              <ValidatedField
                id="purchased-subscription-creator"
                name="creator"
                data-cy="creator"
                label={translate('monsterdamChicksApp.purchasedSubscription.creator')}
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
              <ValidatedField
                id="purchased-subscription-payment"
                name="payment"
                data-cy="payment"
                label={translate('monsterdamChicksApp.purchasedSubscription.payment')}
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
                id="purchased-subscription-subscriptionPlanOffer"
                name="subscriptionPlanOffer"
                data-cy="subscriptionPlanOffer"
                label={translate('monsterdamChicksApp.purchasedSubscription.subscriptionPlanOffer')}
                type="select"
              >
                <option value="" key="0" />
                {subscriptionPlanOffers
                  ? subscriptionPlanOffers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/purchased-subscription" replace color="info">
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

export default PurchasedSubscriptionUpdate;
